package org.summer.sdt.internal.compiler.ast;

import java.util.HashMap;
import java.util.Map;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.lookup.BlockScope;

public class HtmlText  extends HtmlNode {
	public char[] source;

	public HtmlText(char[] source, long pos) {
		super();
		this.source = source;
		//by default the position are the one of the field (not true for super access)
		this.sourceStart = (int) (pos >>> 32);
		this.sourceEnd = (int) (pos & 0x00000000FFFFFFFFL);
	}

	@Override
	public void resolve(BlockScope scope) {
		
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		output.append(source);
		return output;
	}
	
	public char[] translateEntity(boolean reservedReturn){
		boolean flag = false;
		int i = 0, length = 0;
		
		char[] newSrc = CharOperation.eascape(source);
		
		char[] entity = new char[newSrc.length];
		char[] result = new char[newSrc.length];
		for(char currentChar : newSrc){
			switch(currentChar){
			case '&':
				if(flag){
					System.arraycopy(entity, 0, result, length, i);
					length += i;
					i = 0;
				} else {
					flag = true;
				}
				i = 0;
				entity[i++] = currentChar;
				break;
			case ';':
				entity[i++] = currentChar;
				if(flag){
					char c = trans(CharOperation.subarray(entity, 0, i));
					if(c != 0){
						if(c == '"'){
							result[length++] = '\\';
							result[length++] = c;
						} else if(c == '\''){
							result[length++] = '\\';
							result[length++] = c;
						} else {
							result[length++] = c;
						}
					} else {
						System.arraycopy(entity, 0, result, length, i);
						length += i;
					}
				} else {
					result[length++] = currentChar;
				}
				
				i = 0;
				flag = false;
				break;
			default:
				if(flag){
					entity[i++] = currentChar;
				} else {
					result[length++] = currentChar;
				}
			}
		}
		if(i>0){
			System.arraycopy(entity, 0, result, length, i);
			length += i;
		}
		return CharOperation.subarray(result, 0, length);
	}
	
	private static final Map<String, Character> entityMap = new HashMap<String, Character>();
	static{
		entityMap.put("quot", '\"'); //$NON-NLS-1$ 
		entityMap.put("apos", '\''); //$NON-NLS-1$ 
		entityMap.put("lt", '<'); //$NON-NLS-1$ 
		entityMap.put("gt", '>'); //$NON-NLS-1$ 
		entityMap.put("amp", '&'); //$NON-NLS-1$ 
		entityMap.put("nbsp", ' '); //$NON-NLS-1$ 
		entityMap.put("cent", '\uFFE0'); //$NON-NLS-1$ 
		entityMap.put("pound", '£'); //$NON-NLS-1$ 
		entityMap.put("yen", '¥'); //$NON-NLS-1$ 
		entityMap.put("euro", '\u20AC'); //$NON-NLS-1$ 
		entityMap.put("sect", '§'); //$NON-NLS-1$ 
		entityMap.put("trade", '\u2122'); //$NON-NLS-1$ 
		entityMap.put("times", '×'); //$NON-NLS-1$ 
		entityMap.put("divide", '÷'); //$NON-NLS-1$ 
		//TODO to be continue
	}

	private char trans(char[] name) {
		if(name[1] == '#'){
			char[] num = CharOperation.subarray(name, 2, name.length - 1);
			return (char) CharOperation.parseInt(num, 0, num.length);
		} else {
			Character c = entityMap.get(new String(CharOperation.subarray(name, 1, name.length - 1)));
			return c == null ? 0 : c ;
		}
	}
	
	@Override
	public StringBuffer html(BlockScope scope, int indent, StringBuffer output, String _this) {
		return output.append(source);
	}

	@Override
	protected StringBuffer scriptDom(BlockScope scope, int indent, StringBuffer output, String parentNode, String logicParent, String _this) {
		if(hasTrueContent()){
			if(CharOperation.hasContent(source)){
				output.append("$.a(").append(parentNode).append(", ").append("$.t(\"");
				output.append(CharOperation.eascape(source)).append("\"));");
			}	
		}
		return output;
	}

	private boolean hasTrueContent() {
		for(int i=0, length=this.source.length; i<length;i++){
			switch (this.source[i]) {
			case 10 : /* \ u000a: LINE FEED               */
			case 12 : /* \ u000c: FORM FEED               */
			case 13 : /* \ u000d: CARRIAGE RETURN         */
			case 32 : /* \ u0020: SPACE                   */
			case 9 : /* \ u0009: HORIZONTAL TABULATION   */
				break;
			default :
				return true;
			}
		}
		return false;
	}

	@Override
	public StringBuffer option(BlockScope scope, int indent, StringBuffer output, String _this) {
		return output;
	}

}
