package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.html.Js2HtmlMapping;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.javascript.JsConstant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.FieldBinding;
import org.summer.sdt.internal.compiler.lookup.InferenceContext18;
import org.summer.sdt.internal.compiler.lookup.InvocationSite;
import org.summer.sdt.internal.compiler.lookup.MethodBinding;
import org.summer.sdt.internal.compiler.lookup.MissingTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemFieldBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemReasons;
import org.summer.sdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeConstants;
import org.summer.sdt.internal.compiler.lookup.TypeIds;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class HtmlAttribute extends HtmlNode implements InvocationSite{
	public PropertyReference property;
	public Expression value;
	
	public TypeBinding type;   //type of property 
//	public MethodBinding method;
	public ReferenceBinding template;
	public FieldBinding field;
	
	public static final int ENUM = 1;
	public static final int FUNCTION = 2;
	public static final int CLASS = 3;
	public static final int STRING = 4;
	public static final int TEMPLATESETTING = 6;
	public static final int REFERENCE = 7;
	
	public static final int BYTE = 11;
	public static final int SHORT = 12;
	public static final int INT = 13;
	public static final int LONG = 14;
	public static final int FLOAT = 15;
	public static final int DOUBLE = 16;
	public static final int BOOLEAN = 17;
	
	private int propertyKind;
	
	public final static char[] NAME = "name".toCharArray();
	
	public static final int NamedFlag = 1;
	
	public HtmlAttribute(PropertyReference property) {
		super();
		this.property = property;
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		return printStatement(indent, output);
	}
	
	@Override
	public StringBuffer printStatement(int indent, StringBuffer output) {
		printPropertyName(indent, output);
		output.append(" = ");
		value.printExpression(indent, output);
		return output;
	}
	
	protected void printPropertyName(int indent, StringBuffer output) {
		output.append(property.name());
	}

	public void resolve(BlockScope scope) {
		this.constant = Constant.NotAConstant;
		internalResolve(scope);
	}
	
	private void internalResolve(BlockScope scope){
		this.type = property.resolveType(scope);
		
		if((this.tagBits & HtmlBits.NamedField) != 0){
			scope.element.tagBits |= HtmlBits.NamedField;
			this.tagBits |= HtmlBits.NamedField;
		}
		if(this.value instanceof HtmlMarkupExtension){
			HtmlMarkupExtension markExt = (HtmlMarkupExtension) this.value;
			markExt.resolve(scope);
			scope.element.tagBits |= HtmlBits.HasMarkupExtension;
			this.tagBits |= HtmlBits.HasMarkupExtension;
		}

		if(this.type == null){
			return;
		}

		if(this.type.id < TypeIds.T_LastWellKnownTypeId){
			switch(this.type.id){
			case TypeIds.T_byte:
			case TypeIds.T_JavaLangByte:
				propertyKind = BYTE;
				return;
			case TypeIds.T_short:
			case TypeIds.T_JavaLangShort:
				propertyKind = SHORT;
				return;
			case TypeIds.T_int:
			case TypeIds.T_JavaLangInteger:
				propertyKind = INT;
				return;
			case TypeIds.T_long:
			case TypeIds.T_JavaLangLong:
				propertyKind = LONG;
				return;
			case TypeIds.T_float:
			case TypeIds.T_JavaLangFloat:
				propertyKind = FLOAT;
				return;
			case TypeIds.T_double:
			case TypeIds.T_JavaLangDouble:
				propertyKind = DOUBLE;
				return;
			case TypeIds.T_boolean:
			case TypeIds.T_JavaLangBoolean:
				propertyKind = BOOLEAN;
				return;
			case TypeIds.T_JavaLangString:
				propertyKind = STRING;
				return;
			case TypeIds.T_JavaLangClass:
				propertyKind = CLASS;
				this.template = (ReferenceBinding) scope.getType(((Literal) this.value).source());
				return;
			}
		}
		
		if(this.type == scope.environment().getType(TypeConstants.JAVA_LANG_TEMPLATE)){
			this.tagBits |= HtmlBits.HasTemplate;
			propertyKind = TEMPLATESETTING;
			if(this.value instanceof HtmlMarkupExtension){
				
			} else {
				this.template = (ReferenceBinding) scope.getType(((Literal) this.value).source());
				return;
			}
		}
		
		if(this.value instanceof HtmlFunctionExpression){
			HtmlFunctionExpression function = (HtmlFunctionExpression) this.value;
			function.resolve(scope);
			scope.element.tagBits |= HtmlBits.FunctionExpression;
			return;
		}
		
		ReferenceBinding refType = (ReferenceBinding) this.type;
		if((refType.modifiers & ClassFileConstants.AccFunction) != 0 ){
			propertyKind = FUNCTION;
			this.tagBits |= HtmlBits.EventCallback;
			Literal stringLiteral = (Literal) this.value;
			this.field = scope.findField(scope.classScope().referenceContext.binding, stringLiteral.source(), this, true);
			if(field == null){
				scope.problemReporter().errorNoMethodFor(stringLiteral, this.type, new TypeBinding[0]); 
			}
		} else if(refType.isEnum()){
			propertyKind = ENUM;
			this.field = scope.getField(refType, ((Literal) this.value).source(), this);
			if((field.modifiers & ClassFileConstants.AccEnum) == 0) {
				scope.problemReporter().invalidProperty(property, property.binding.type);   //TODO  cym 2015-02-27
			}
			if(field == null){
				scope.problemReporter().invalidProperty(property, property.binding.type);    //TODO  cym 2015-02-27
			}
			
			if (!field.isValidBinding()) {
				this.constant = Constant.NotAConstant;
				if (refType instanceof ProblemReferenceBinding) {
					// problem already got signaled on receiver, do not report secondary problem
					return;
				}
				// https://bugs.eclipse.org/bugs/show_bug.cgi?id=245007 avoid secondary errors in case of
				// missing super type for anonymous classes ... 
				ReferenceBinding declaringClass = field.declaringClass;
				boolean avoidSecondary = declaringClass != null &&
										 declaringClass.isAnonymousType() &&
										 declaringClass.superclass() instanceof MissingTypeBinding;
				if (!avoidSecondary) {
					scope.problemReporter().invalidProperty(property, property.binding.type);  //TODO cym 2015-02-27
				}
				if (field instanceof ProblemFieldBinding) {
					ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) field;
					FieldBinding closestMatch = problemFieldBinding.closestMatch;
					switch(problemFieldBinding.problemId()) {
						case ProblemReasons.InheritedNameHidesEnclosingName :
						case ProblemReasons.NotVisible :
						case ProblemReasons.NonStaticReferenceInConstructorInvocation :
						case ProblemReasons.NonStaticReferenceInStaticContext :
							if (closestMatch != null) {
								field = closestMatch;
							}
					}
				}
				if (!field.isValidBinding()) {
					return;
				}
			}
		} else if(this.type instanceof ParameterizedTypeBinding && ((ParameterizedTypeBinding)this.type).original().id == TypeIds.T_JavaLangClass) {
			propertyKind = CLASS;
			this.template = (ReferenceBinding) scope.getType(((Literal) this.value).source());
		}  else {
			propertyKind = REFERENCE;
			if(this.value instanceof HtmlMarkupExtension){
				
			} else {
				this.template = (ReferenceBinding) scope.getType(((Literal) this.value).source());
			}
		}
	}
	
	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append(Js2HtmlMapping.getHtmlName(new String(property.name()))).append("=");
		
		if(this.property.binding != null && this.property.binding.type instanceof ReferenceBinding 
				&& (((ReferenceBinding)this.property.binding.type).modifiers & ClassFileConstants.AccFunction) != 0){
			output.append("\"__this.");
			if(this.value instanceof Literal){
				output.append(((Literal)this.value).source());
			}
			output.append("(event);\"");
		} else {
			value.generateExpression(scope, indent, output);
		}
		
		return output;
	}
	
	public StringBuffer buildProperty(Scope scope, int indent, StringBuffer output, String context) {
		output.append(Js2HtmlMapping.getHtmlName(new String(property.name()))).append("=");
		
		if(this.property.binding != null && this.property.binding.type instanceof ReferenceBinding 
				&& (((ReferenceBinding)this.property.binding.type).modifiers & ClassFileConstants.AccFunction) != 0){
			output.append("\"").append(context).append('.');
			if(this.value instanceof Literal){
				output.append(((Literal)this.value).source());
			}
			output.append("(event);\"");
		} else {
			value.generateExpression(scope, indent, output);
		}
		
		return output;
	}
	
	public StringBuffer buildMarkupExtensionOptionPart(Scope scope, int indent, StringBuffer output){
		
		this.property.generateExpression(scope, indent, output).append(" : ");
		switch(propertyKind){
		case CLASS:
			this.template.generate(output, null);
			output.append(".prototype.__class");
			return output;
		case ENUM:
			((ReferenceBinding)this.property.binding.type).generate(output, null);
			output.append('.').append(((Literal) this.value).source());
			return output;
		case FUNCTION:
//			if(this.method != null){
//				if(this.method.isStatic()){
//					output.append(this.method.declaringClass.sourceName).append('.');
//					if(this.value instanceof Literal){
//						output.append(((Literal)this.value).source());
//					}
//				} else {
////					output.append("this.");
//					output.append("__this.");
//					if(this.value instanceof Literal){
//						output.append(((Literal)this.value).source());
//					}
//				}
//			} else 
			if(this.field != null){
				if(this.field.isStatic()){
					output.append(this.field.declaringClass.sourceName).append('.');
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				} else {
//					output.append("this.");
					output.append("__this.");
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				}
			}
			return output;
		case TEMPLATESETTING:
			output.append("new (");
			this.template.generate(output, null);
			output.append(")()");
			return output;
		case REFERENCE:
		case BYTE:
		case SHORT:
		case INT:
		case LONG:
		case FLOAT:
		case DOUBLE:
		case BOOLEAN:
		case STRING:
			if(this.value instanceof HtmlMarkupExtension){
				HtmlMarkupExtension me = (HtmlMarkupExtension) value;
				me.doGenerateExpression(scope, indent, output);  //TODO 2015-03-03 cym
				output.append(".provideValue(");
				output.append("_n, \""); 
				this.property.buildInjectPart(scope, indent, output);
				output.append(");");
			} else {
				Literal s = (Literal) this.value;
				if(s instanceof StringLiteral){
					output.append("\"").append(s.source()).append("\"");
				} else if(s instanceof NumberLiteral){
					if(s instanceof IntLiteral){
						output.append(((IntLiteral)s).value);
					} else if(s instanceof LongLiteral){
						output.append(s.constant.longValue());
					} else if(s instanceof FloatLiteral){
						output.append(((FloatLiteral)s).value);
					} else if(s instanceof DoubleLiteral){
						output.append(((DoubleLiteral)s).value);
					}
				} else {
					output.append(s.source());
				}
			}
		}
			
		return output;
	}
	
	public void buildScript(Scope scope, int indent, StringBuffer output, String node){
		if(this.value instanceof HtmlMarkupExtension){
			output.append("\n");
			printIndent(indent + 1, output);
			HtmlMarkupExtension me = (HtmlMarkupExtension) this.value;
			me.doGenerateExpression(scope, indent, output);
			
			output.append(".inject(");
			output.append(JsConstant.NODE_NAME).append(", ");
			this.property.buildInjectPart(scope, indent, output);
			output.append(");");
			return;
		} else if((this.tagBits & HtmlBits.NamedField) != 0){
			output.append("\n");
			printIndent(indent + 1, output);
			output.append(node).append("[\"");
			this.property.generateExpression(scope, indent, output);
			output.append("\"]");
			output.append(" = ");
			if(this.value instanceof Literal){
				this.value.generateExpression(scope, indent, output);
			}
			output.append(";");
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("this");
			output.append('.').append(((Literal) this.value).source());
			output.append(" = ").append(node).append(";");
			return;
		} 
//		else if(this.value instanceof HtmlFunctionExpression){
//			
//		}
		
		switch(propertyKind){
//		case TEMPLATESETTING:
//			output.append("\n");
//			printIndent(indent + 2, output);
//			HtmlMarkupExtension me = (HtmlMarkupExtension) this.value;
//			me.doGenerateExpression(scope, indent, output);
//			
//			output.append(".inject(");
//			output.append("n, ");
//			this.property.buildInMarkupExtension(scope, indent, output);
//			output.append(");");
//			return; 
		case FUNCTION:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append(JsConstant.NODE_NAME).append(".addEventListener('").append(CharOperation.subarray(this.property.name(), 2, -1)).append("', ");
			if(this.field != null){
				if(this.field.isStatic()){
					output.append(this.field.declaringClass.sourceName).append('.');
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				} else {
					output.append("this.");
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				}
			}
			output.append(", false);");
			break;
		case CLASS:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append(JsConstant.NODE_NAME).append('.');
			this.property.doGenerateExpression(scope, indent, output);
			
			output.append(" = ");
			((ReferenceBinding)this.property.binding.type).generate(output, null);
			output.append(".prototype.__class;");
			break;
		case ENUM:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append(JsConstant.NODE_NAME).append('.');
			this.property.doGenerateExpression(scope, indent, output);
			output.append(" = ");
			((ReferenceBinding)this.property.binding.type).generate(output, null);
			output.append('.').append(((Literal) this.value).source());
			break;
		case REFERENCE:
		case STRING:
		case BYTE:
		case SHORT:
		case INT:
		case LONG:
		case FLOAT:
		case DOUBLE:
		case BOOLEAN:
			output.append("\n");
			printIndent(indent + 1, output);
//			output.append(JsConstant.NODE_NAME).append('.');
//			this.property.doGenerateExpression(scope, indent, output);
//			output.append(" = ");
//			this.value.generateExpression(scope, indent, output).append(";");
			
			output.append("$.attr(").append(JsConstant.NODE_NAME).append(',');
			output.append(" \"");
			this.property.doGenerateExpression(scope, indent, output).append("\", ");
			this.value.generateExpression(scope, indent, output).append(");");
		}
	}
	
	@Override
	public StringBuffer html(BlockScope scope, int indent, StringBuffer output) {
		if((this.tagBits & HtmlBits.HasDynamicContent) != 0){
			return output;
		}
		
		this.property.html(scope, indent, output);
		output.append("=");
		switch(propertyKind){
		case FUNCTION:
			output.append("\"");
			if(this.field != null){
				if(this.field.isStatic()){
					this.field.declaringClass.generate(output, scope.classScope().enclosingSourceType());
					output.append('.');
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				} else {
					output.append("__this.");
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				}
			}
			output.append("(event);\" ");
			break;
		case CLASS:
		case ENUM:
		case REFERENCE:
		case STRING:
		case BYTE:
		case SHORT:
		case INT:
		case LONG:
		case FLOAT:
		case DOUBLE:
		case BOOLEAN:
		default:
			this.value.generateExpression(scope, indent, output);
		}
		return output;
	}
	
	public ASTNode parserElement(int position){
		if(this.property.sourceStart < position && this.property.sourceEnd >= position){
			return this.property;
		} 
		if(this.value.sourceStart < position && this.value.sourceEnd >= position){
			if(this.value instanceof HtmlMarkupExtension){
				HtmlMarkupExtension markup = (HtmlMarkupExtension) this.value;
				return markup.parserElement(position);
			}
			return this.value;
		}
		return null;
	}
	
	@Override
	public StringBuffer generateStatement(Scope scope, int indent, StringBuffer output) {
		generateExpression(scope, indent, output);
		return output;
	}
	
	@Override
	public StringBuffer generateExpression(Scope scope, int indent, StringBuffer output) {
		return doGenerateExpression(scope, indent, output);
	}
	
	public TypeBinding[] genericTypeArguments() { return null;}
	public boolean isSuperAccess() {return false;}
	public boolean isTypeAccess() {return false;}
	public void setActualReceiverType(ReferenceBinding receiverType) {/* empty */}
	public void setDepth(int depth) {/* empty */ }
	public void setFieldIndex(int depth) {/* empty */ }
	public int sourceEnd() {return this.sourceEnd; }
	public int sourceStart() {return this.sourceStart; }
	public TypeBinding invocationTargetType() { return null; }
	public boolean receiverIsImplicitThis() { return false; }
	public InferenceContext18 freshInferenceContext(Scope scope) { return null; }
	public ExpressionContext getExpressionContext() { return ExpressionContext.VANILLA_CONTEXT; }
	@Override
	public boolean isQualifiedSuper() { return false; }
	public boolean checkingPotentialCompatibility() { return false; }
	public void acceptPotentiallyCompatibleMethods(MethodBinding[] methods) { /* ignore */ }

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return output;
	}
}
