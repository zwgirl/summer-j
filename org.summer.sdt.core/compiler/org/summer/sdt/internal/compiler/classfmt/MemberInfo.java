package org.summer.sdt.internal.compiler.classfmt;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.codegen.AttributeNamesConstants;
import org.summer.sdt.internal.compiler.codegen.ConstantPool;
import org.summer.sdt.internal.compiler.env.IBinaryAnnotation;
import org.summer.sdt.internal.compiler.env.IBinaryMember;
import org.summer.sdt.internal.compiler.env.IBinaryTypeAnnotation;
import org.summer.sdt.internal.compiler.impl.BooleanConstant;
import org.summer.sdt.internal.compiler.impl.ByteConstant;
import org.summer.sdt.internal.compiler.impl.CharConstant;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.impl.DoubleConstant;
import org.summer.sdt.internal.compiler.impl.FloatConstant;
import org.summer.sdt.internal.compiler.impl.IntConstant;
import org.summer.sdt.internal.compiler.impl.LongConstant;
import org.summer.sdt.internal.compiler.impl.ShortConstant;
import org.summer.sdt.internal.compiler.impl.StringConstant;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeIds;
import org.summer.sdt.internal.compiler.util.Util;

@SuppressWarnings("rawtypes")
public class MemberInfo  extends ClassFileStruct implements IBinaryMember, Comparable {

	public MemberInfo(byte[] classFileBytes, int[] offsets, int offset) {
		super(classFileBytes, offsets, offset);
		// TODO Auto-generated constructor stub
	}

	protected int accessFlags;
	protected int attributeBytes;
	protected Constant constant;
	protected char[] descriptor;
	protected char[] name;
	protected char[] signature;
	protected int signatureUtf8Offset;
	protected long tagBits;
	protected Object wrappedConstantValue;
	
	//cym 2014-12-04 for indexer parameters
	static private final char[][] noParameters = CharOperation.NO_CHAR_CHAR;
	protected char[][] parameters = noParameters;
	
	
	static private final char[][] noException = CharOperation.NO_CHAR_CHAR;
	static private final char[][] noArgumentNames = CharOperation.NO_CHAR_CHAR;
	static private final char[] ARG = "arg".toCharArray();  //$NON-NLS-1$
	protected char[][] exceptionNames;
	protected char[][] argumentNames;

	public static MemberInfo createField(byte classFileBytes[], int offsets[], int offset) {
		MemberInfo fieldInfo = new MemberInfo(classFileBytes, offsets, offset);
		
		int attributesCount = fieldInfo.u2At(6);
		int readOffset = 8;
		AnnotationInfo[] annotations = null;
		TypeAnnotationInfo[] typeAnnotations = null;
		for (int i = 0; i < attributesCount; i++) {
			// check the name of each attribute
			int utf8Offset = fieldInfo.constantPoolOffsets[fieldInfo.u2At(readOffset)] - fieldInfo.structOffset;
			char[] attributeName = fieldInfo.utf8At(utf8Offset + 3, fieldInfo.u2At(utf8Offset + 1));
			if (attributeName.length > 0) {
				switch(attributeName[0]) {
					case 'S' :
						if (CharOperation.equals(AttributeNamesConstants.SignatureName, attributeName))
							fieldInfo.signatureUtf8Offset = fieldInfo.constantPoolOffsets[fieldInfo.u2At(readOffset + 6)] - fieldInfo.structOffset;
						break;
					case 'E' :  //Exceptions  cym 2014-12-04
						if (CharOperation.equals(attributeName, AttributeNamesConstants.ExceptionsName)) {
							// read the number of exception entries
							int entriesNumber = fieldInfo.u2At(readOffset + 6);
							// place the readOffset at the beginning of the exceptions table
							int pos = readOffset + 8;
							
							if (entriesNumber == 0) {
								fieldInfo.parameters = noParameters;
							} else {
								fieldInfo.parameters = new char[entriesNumber][];
								for (int j = 0; j < entriesNumber; j++) {
									utf8Offset =
											fieldInfo.constantPoolOffsets[fieldInfo.u2At(
													fieldInfo.constantPoolOffsets[fieldInfo.u2At(pos)] - fieldInfo.structOffset + 1)]
											- fieldInfo.structOffset;
									fieldInfo.parameters[j] = fieldInfo.utf8At(utf8Offset + 3, fieldInfo.u2At(utf8Offset + 1));
									pos += 2;
								}
							}
						}
						break;
						
					case 'R' :
						AnnotationInfo[] decodedAnnotations = null;
						TypeAnnotationInfo[] decodedTypeAnnotations = null;
						if (CharOperation.equals(attributeName, AttributeNamesConstants.RuntimeVisibleAnnotationsName)) {
							decodedAnnotations = fieldInfo.decodeAnnotations(readOffset, true);
						} else if (CharOperation.equals(attributeName, AttributeNamesConstants.RuntimeInvisibleAnnotationsName)) {
							decodedAnnotations = fieldInfo.decodeAnnotations(readOffset, false);
						} else if (CharOperation.equals(attributeName, AttributeNamesConstants.RuntimeVisibleTypeAnnotationsName)) {
							decodedTypeAnnotations = fieldInfo.decodeTypeAnnotations(readOffset, true);
						} else if (CharOperation.equals(attributeName, AttributeNamesConstants.RuntimeInvisibleTypeAnnotationsName)) {
							decodedTypeAnnotations = fieldInfo.decodeTypeAnnotations(readOffset, false);
						}
						if (decodedAnnotations != null) {
							if (annotations == null) {
								annotations = decodedAnnotations;
							} else {
								int length = annotations.length;
								AnnotationInfo[] combined = new AnnotationInfo[length + decodedAnnotations.length];
								System.arraycopy(annotations, 0, combined, 0, length);
								System.arraycopy(decodedAnnotations, 0, combined, length, decodedAnnotations.length);
								annotations = combined;
							}
						} else if (decodedTypeAnnotations != null) {
							if (typeAnnotations == null) {
								typeAnnotations = decodedTypeAnnotations;
							} else {
								int length = typeAnnotations.length;
								TypeAnnotationInfo[] combined = new TypeAnnotationInfo[length + decodedTypeAnnotations.length];
								System.arraycopy(typeAnnotations, 0, combined, 0, length);
								System.arraycopy(decodedTypeAnnotations, 0, combined, length, decodedTypeAnnotations.length);
								typeAnnotations = combined;
							}
						}
				}
			}
			readOffset += (6 + fieldInfo.u4At(readOffset + 2));
		}
		fieldInfo.attributeBytes = readOffset;
		
		if (typeAnnotations != null)
			return new FieldInfoWithTypeAnnotation(fieldInfo, annotations, typeAnnotations);
		if (annotations != null)
			return new FieldInfoWithAnnotation(fieldInfo, annotations);
		return fieldInfo;
	}
	
	/**
	 * @param classFileBytes byte[]
	 * @param offsets int[]
	 * @param offset int
	 */
	protected MemberInfo (byte classFileBytes[], int offsets[], int offset) {
		super(classFileBytes, offsets, offset);
		this.accessFlags = -1;
		this.signatureUtf8Offset = -1;
	}
	private AnnotationInfo[] decodeAnnotations(int offset, boolean runtimeVisible) {
		int numberOfAnnotations = u2At(offset + 6);
		if (numberOfAnnotations > 0) {
			int readOffset = offset + 8;
			AnnotationInfo[] newInfos = null;
			int newInfoCount = 0;
			for (int i = 0; i < numberOfAnnotations; i++) {
				// With the last parameter being 'false', the data structure will not be flushed out
				AnnotationInfo newInfo = new AnnotationInfo(this.reference, this.constantPoolOffsets,
					readOffset + this.structOffset, runtimeVisible, false);
				readOffset += newInfo.readOffset;
				long standardTagBits = newInfo.standardAnnotationTagBits;
				if (standardTagBits != 0) {
					this.tagBits |= standardTagBits;
				} else {
					if (newInfos == null)
						newInfos = new AnnotationInfo[numberOfAnnotations - i];
					newInfos[newInfoCount++] = newInfo;
				}
			}
			if (newInfos != null) {
				if (newInfoCount != newInfos.length)
					System.arraycopy(newInfos, 0, newInfos = new AnnotationInfo[newInfoCount], 0, newInfoCount);
				return newInfos;
			}
		}
		return null; // nothing to record
	}
	
	TypeAnnotationInfo[] decodeTypeAnnotations(int offset, boolean runtimeVisible) {
		int numberOfAnnotations = u2At(offset + 6);
		if (numberOfAnnotations > 0) {
			int readOffset = offset + 8;
			TypeAnnotationInfo[] typeAnnos = new TypeAnnotationInfo[numberOfAnnotations];
			for (int i = 0; i < numberOfAnnotations; i++) {
				TypeAnnotationInfo newInfo = new TypeAnnotationInfo(this.reference, this.constantPoolOffsets, readOffset + this.structOffset, runtimeVisible, false);
				readOffset += newInfo.readOffset;
				typeAnnos[i] = newInfo;
			}
			return typeAnnos;
		}
		return null;
	}
	
	public int compareTo(Object o) {
		return new String(getName()).compareTo(new String(((FieldInfo) o).getName()));
	}
	public boolean equals(Object o) {
		if (!(o instanceof FieldInfo)) {
			return false;
		}
		return CharOperation.equals(getName(), ((FieldInfo) o).getName());
	}
	public int hashCode() {
		return CharOperation.hashCode(getName());
	}
	/**
	 * Return the constant of the field.
	 * Return org.summer.sdt.internal.compiler.impl.Constant.NotAConstant if there is none.
	 * @return org.summer.sdt.internal.compiler.impl.Constant
	 */
	public Constant getConstant() {
		if (this.constant == null) {
			// read constant
			readConstantAttribute();
		}
		return this.constant;
	}
	public char[] getGenericSignature() {
		if (this.signatureUtf8Offset != -1) {
			if (this.signature == null) {
				// decode the signature
				this.signature = utf8At(this.signatureUtf8Offset + 3, u2At(this.signatureUtf8Offset + 1));
			}
			return this.signature;
		}
		return null;
	}
	/**
	 * Answer an int whose bits are set according the access constants
	 * defined by the VM spec.
	 * Set the AccDeprecated and AccSynthetic bits if necessary
	 * @return int
	 */
	public int getModifiers() {
		if (this.accessFlags == -1) {
			// compute the accessflag. Don't forget the deprecated attribute
			this.accessFlags = u2At(0);
			readModifierRelatedAttributes();
		}
		return this.accessFlags;
	}
	/**
	 * Answer the name of the field.
	 * @return char[]
	 */
	public char[] getName() {
		if (this.name == null) {
			// read the name
			int utf8Offset = this.constantPoolOffsets[u2At(2)] - this.structOffset;
			this.name = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
		}
		return this.name;
	}
	public long getTagBits() {
		return this.tagBits;
	}
	/**
	 * Answer the resolved name of the receiver's type in the
	 * class file format as specified in section 4.3.2 of the Java 2 VM spec.
	 *
	 * For example:
	 *   - java.lang.String is Ljava/lang/String;
	 *   - an int is I
	 *   - a 2 dimensional array of strings is [[Ljava/lang/String;
	 *   - an array of floats is [F
	 * @return char[]
	 */
	public char[] getTypeName() {
		if (this.descriptor == null) {
			// read the signature
			int utf8Offset = this.constantPoolOffsets[u2At(4)] - this.structOffset;
			this.descriptor = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
		}
		return this.descriptor;
	}
	/**
	 * @return the annotations or null if there is none.
	 */
	public IBinaryAnnotation[] getAnnotations() {
		return null;
	}
	
	public IBinaryTypeAnnotation[] getTypeAnnotations() {
		return null;
	}
	/**
	 * Return a wrapper that contains the constant of the field.
	 * @return java.lang.Object
	 */
	public Object getWrappedConstantValue() {
	
		if (this.wrappedConstantValue == null) {
			if (hasConstant()) {
				Constant fieldConstant = getConstant();
				switch (fieldConstant.typeID()) {
					case TypeIds.T_int :
						this.wrappedConstantValue = new Integer(fieldConstant.intValue());
						break;
					case TypeIds.T_byte :
						this.wrappedConstantValue = new Byte(fieldConstant.byteValue());
						break;
					case TypeIds.T_short :
						this.wrappedConstantValue = new Short(fieldConstant.shortValue());
						break;
					case TypeIds.T_char :
						this.wrappedConstantValue = new Character(fieldConstant.charValue());
						break;
					case TypeIds.T_float :
						this.wrappedConstantValue = new Float(fieldConstant.floatValue());
						break;
					case TypeIds.T_double :
						this.wrappedConstantValue = new Double(fieldConstant.doubleValue());
						break;
					case TypeIds.T_boolean :
						this.wrappedConstantValue = Util.toBoolean(fieldConstant.booleanValue());
						break;
					case TypeIds.T_long :
						this.wrappedConstantValue = new Long(fieldConstant.longValue());
						break;
					case TypeIds.T_JavaLangString :
						this.wrappedConstantValue = fieldConstant.stringValue();
				}
			}
		}
		return this.wrappedConstantValue;
	}
	/**
	 * Return true if the field has a constant value attribute, false otherwise.
	 * @return boolean
	 */
	public boolean hasConstant() {
		return getConstant() != Constant.NotAConstant;
	}
	/**
	 * This method is used to fully initialize the contents of the receiver. All methodinfos, fields infos
	 * will be therefore fully initialized and we can get rid of the bytes.
	 */
	protected void initialize() {
		getModifiers();
		getName();
		getConstant();
		getTypeName();
		getGenericSignature();
		reset();
	}
	/**
	 * Return true if the field is a synthetic field, false otherwise.
	 * @return boolean
	 */
	public boolean isSynthetic() {
		return (getModifiers() & ClassFileConstants.AccSynthetic) != 0;
	}
	private void readConstantAttribute() {
		int attributesCount = u2At(6);
		int readOffset = 8;
		boolean isConstant = false;
		for (int i = 0; i < attributesCount; i++) {
			int utf8Offset = this.constantPoolOffsets[u2At(readOffset)] - this.structOffset;
			char[] attributeName = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
			if (CharOperation
				.equals(attributeName, AttributeNamesConstants.ConstantValueName)) {
				isConstant = true;
				// read the right constant
				int relativeOffset = this.constantPoolOffsets[u2At(readOffset + 6)] - this.structOffset;
				switch (u1At(relativeOffset)) {
					case ClassFileConstants.IntegerTag :
						char[] sign = getTypeName();
						if (sign.length == 1) {
							switch (sign[0]) {
								case 'Z' : // boolean constant
									this.constant = BooleanConstant.fromValue(i4At(relativeOffset + 1) == 1);
									break;
								case 'I' : // integer constant
									this.constant = IntConstant.fromValue(i4At(relativeOffset + 1));
									break;
								case 'C' : // char constant
									this.constant = CharConstant.fromValue((char) i4At(relativeOffset + 1));
									break;
								case 'B' : // byte constant
									this.constant = ByteConstant.fromValue((byte) i4At(relativeOffset + 1));
									break;
								case 'S' : // short constant
									this.constant = ShortConstant.fromValue((short) i4At(relativeOffset + 1));
									break;
								default:
									this.constant = Constant.NotAConstant;
							}
						} else {
							this.constant = Constant.NotAConstant;
						}
						break;
					case ClassFileConstants.FloatTag :
						this.constant = FloatConstant.fromValue(floatAt(relativeOffset + 1));
						break;
					case ClassFileConstants.DoubleTag :
						this.constant = DoubleConstant.fromValue(doubleAt(relativeOffset + 1));
						break;
					case ClassFileConstants.LongTag :
						this.constant = LongConstant.fromValue(i8At(relativeOffset + 1));
						break;
					case ClassFileConstants.StringTag :
						utf8Offset = this.constantPoolOffsets[u2At(relativeOffset + 1)] - this.structOffset;
						this.constant =
							StringConstant.fromValue(
								String.valueOf(utf8At(utf8Offset + 3, u2At(utf8Offset + 1))));
						break;
				}
			}
			readOffset += (6 + u4At(readOffset + 2));
		}
		if (!isConstant) {
			this.constant = Constant.NotAConstant;
		}
	}
	private void readModifierRelatedAttributes() {
		int attributesCount = u2At(6);
		int readOffset = 8;
		for (int i = 0; i < attributesCount; i++) {
			int utf8Offset = this.constantPoolOffsets[u2At(readOffset)] - this.structOffset;
			char[] attributeName = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
			// test added for obfuscated .class file. See 79772
			if (attributeName.length != 0) {
				switch(attributeName[0]) {
					case 'D' :
						if (CharOperation.equals(attributeName, AttributeNamesConstants.DeprecatedName))
							this.accessFlags |= ClassFileConstants.AccDeprecated;
						break;
					case 'S' :
						if (CharOperation.equals(attributeName, AttributeNamesConstants.SyntheticName))
							this.accessFlags |= ClassFileConstants.AccSynthetic;
						break;
				}
			}
			readOffset += (6 + u4At(readOffset + 2));
		}
	}
	/**
	 * Answer the size of the receiver in bytes.
	 *
	 * @return int
	 */
	public int sizeInBytes() {
		return this.attributeBytes;
	}
	public void throwFormatException() throws ClassFormatException {
		throw new ClassFormatException(ClassFormatException.ErrBadFieldInfo);
	}
	public String toString() {
		StringBuffer buffer = new StringBuffer(getClass().getName());
		toStringContent(buffer);
		return buffer.toString();
	}
	protected void toStringContent(StringBuffer buffer) {
		int modifiers = getModifiers();
		buffer
			.append('{')
			.append(
				((modifiers & ClassFileConstants.AccDeprecated) != 0 ? "deprecated " : Util.EMPTY_STRING) //$NON-NLS-1$
					+ ((modifiers & 0x0001) == 1 ? "public " : Util.EMPTY_STRING) //$NON-NLS-1$
					+ ((modifiers & 0x0002) == 0x0002 ? "private " : Util.EMPTY_STRING) //$NON-NLS-1$
					+ ((modifiers & 0x0004) == 0x0004 ? "protected " : Util.EMPTY_STRING) //$NON-NLS-1$
					+ ((modifiers & 0x0008) == 0x000008 ? "static " : Util.EMPTY_STRING) //$NON-NLS-1$
					+ ((modifiers & 0x0010) == 0x0010 ? "final " : Util.EMPTY_STRING) //$NON-NLS-1$
					+ ((modifiers & 0x0040) == 0x0040 ? "volatile " : Util.EMPTY_STRING) //$NON-NLS-1$
					+ ((modifiers & 0x0080) == 0x0080 ? "transient " : Util.EMPTY_STRING)) //$NON-NLS-1$
			.append(getTypeName())
			.append(' ')
			.append(getName())
			.append(' ')
			.append(getConstant())
			.append('}')
			.toString();
	}
	
	//cym 2014-12-05
	public char[][] getParameters() {
		return this.parameters;
	}
	
	
	//！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
	


	public static MethodInfo createMethod(byte classFileBytes[], int offsets[], int offset) {
		MethodInfo methodInfo = new MethodInfo(classFileBytes, offsets, offset);
		int attributesCount = methodInfo.u2At(6);
		int readOffset = 8;
		AnnotationInfo[] annotations = null;
		AnnotationInfo[][] parameterAnnotations = null;
		TypeAnnotationInfo[] typeAnnotations = null;
		for (int i = 0; i < attributesCount; i++) {
			// check the name of each attribute
			int utf8Offset = methodInfo.constantPoolOffsets[methodInfo.u2At(readOffset)] - methodInfo.structOffset;
			char[] attributeName = methodInfo.utf8At(utf8Offset + 3, methodInfo.u2At(utf8Offset + 1));
			if (attributeName.length > 0) {
				switch(attributeName[0]) {
					case 'M' :
						if (CharOperation.equals(attributeName, AttributeNamesConstants.MethodParametersName)) {
							methodInfo.decodeMethodParameters(readOffset, methodInfo);
						}
						break;
					case 'S' :
						if (CharOperation.equals(AttributeNamesConstants.SignatureName, attributeName))
							methodInfo.signatureUtf8Offset = methodInfo.constantPoolOffsets[methodInfo.u2At(readOffset + 6)] - methodInfo.structOffset;
						break;
					case 'R' :
						AnnotationInfo[] methodAnnotations = null;
						AnnotationInfo[][] paramAnnotations = null;
						TypeAnnotationInfo[] methodTypeAnnotations = null;
						if (CharOperation.equals(attributeName, AttributeNamesConstants.RuntimeVisibleAnnotationsName)) {
							methodAnnotations = decodeMethodAnnotations(readOffset, true, methodInfo);
						} else if (CharOperation.equals(attributeName, AttributeNamesConstants.RuntimeInvisibleAnnotationsName)) {
							methodAnnotations = decodeMethodAnnotations(readOffset, false, methodInfo);
						} else if (CharOperation.equals(attributeName, AttributeNamesConstants.RuntimeVisibleParameterAnnotationsName)) {
							paramAnnotations = decodeParamAnnotations(readOffset, true, methodInfo);
						} else if (CharOperation.equals(attributeName, AttributeNamesConstants.RuntimeInvisibleParameterAnnotationsName)) {
							paramAnnotations = decodeParamAnnotations(readOffset, false, methodInfo);
						} else if (CharOperation.equals(attributeName, AttributeNamesConstants.RuntimeVisibleTypeAnnotationsName)) {
							methodTypeAnnotations = decodeTypeAnnotations(readOffset, true, methodInfo);
						} else if (CharOperation.equals(attributeName, AttributeNamesConstants.RuntimeInvisibleTypeAnnotationsName)) {
							methodTypeAnnotations = decodeTypeAnnotations(readOffset, false, methodInfo);
						}
						if (methodAnnotations != null) {
							if (annotations == null) {
								annotations = methodAnnotations;
							} else {
								int length = annotations.length;
								AnnotationInfo[] newAnnotations = new AnnotationInfo[length + methodAnnotations.length];
								System.arraycopy(annotations, 0, newAnnotations, 0, length);
								System.arraycopy(methodAnnotations, 0, newAnnotations, length, methodAnnotations.length);
								annotations = newAnnotations;
							}
						} else if (paramAnnotations != null) {
							int numberOfParameters = paramAnnotations.length;
							if (parameterAnnotations == null) {
								parameterAnnotations = paramAnnotations;
							} else {
								for (int p = 0; p < numberOfParameters; p++) {
									int numberOfAnnotations = paramAnnotations[p] == null ? 0 : paramAnnotations[p].length;
									if (numberOfAnnotations > 0) {
										if (parameterAnnotations[p] == null) {
											parameterAnnotations[p] = paramAnnotations[p];
										} else {
											int length = parameterAnnotations[p].length;
											AnnotationInfo[] newAnnotations = new AnnotationInfo[length + numberOfAnnotations];
											System.arraycopy(parameterAnnotations[p], 0, newAnnotations, 0, length);
											System.arraycopy(paramAnnotations[p], 0, newAnnotations, length, numberOfAnnotations);
											parameterAnnotations[p] = newAnnotations;
										}
									}
								}
							}
						} else if (methodTypeAnnotations != null) {
							if (typeAnnotations == null) {
								typeAnnotations = methodTypeAnnotations;
							} else {
								int length = typeAnnotations.length;
								TypeAnnotationInfo[] newAnnotations = new TypeAnnotationInfo[length + methodTypeAnnotations.length];
								System.arraycopy(typeAnnotations, 0, newAnnotations, 0, length);
								System.arraycopy(methodTypeAnnotations, 0, newAnnotations, length, methodTypeAnnotations.length);
								typeAnnotations = newAnnotations;
							}
						}
						break;
				}
			}
			readOffset += (6 + methodInfo.u4At(readOffset + 2));
		}
		methodInfo.attributeBytes = readOffset;
	
		if (typeAnnotations != null)
			return new MethodInfoWithTypeAnnotations(methodInfo, annotations, parameterAnnotations, typeAnnotations);
		if (parameterAnnotations != null)
			return new MethodInfoWithParameterAnnotations(methodInfo, annotations, parameterAnnotations);
		if (annotations != null)
			return new MethodInfoWithAnnotations(methodInfo, annotations);
		return methodInfo;
	}
	static AnnotationInfo[] decodeAnnotations(int offset, boolean runtimeVisible, int numberOfAnnotations, MethodInfo methodInfo) {
		AnnotationInfo[] result = new AnnotationInfo[numberOfAnnotations];
		int readOffset = offset;
		for (int i = 0; i < numberOfAnnotations; i++) {
			result[i] = new AnnotationInfo(methodInfo.reference, methodInfo.constantPoolOffsets,
				readOffset + methodInfo.structOffset, runtimeVisible, false); 
			readOffset += result[i].readOffset;
		}
		return result;
	}
	static AnnotationInfo[] decodeMethodAnnotations(int offset, boolean runtimeVisible, MethodInfo methodInfo) {
		int numberOfAnnotations = methodInfo.u2At(offset + 6);
		if (numberOfAnnotations > 0) {
			AnnotationInfo[] annos = decodeAnnotations(offset + 8, runtimeVisible, numberOfAnnotations, methodInfo);
			if (runtimeVisible){
				int numStandardAnnotations = 0;
				for( int i=0; i<numberOfAnnotations; i++ ){
					long standardAnnoTagBits = annos[i].standardAnnotationTagBits;
					methodInfo.tagBits |= standardAnnoTagBits;
					
					//cym 2015-01-15
//					if(standardAnnoTagBits != 0){
//						annos[i] = null;
//						numStandardAnnotations ++;
//					}
					
					if(standardAnnoTagBits != 0 && (standardAnnoTagBits & TagBits.AnnotationOverload) == 0){
						annos[i] = null;
						numStandardAnnotations ++;
					}
				}
	
				if( numStandardAnnotations != 0 ){
					if( numStandardAnnotations == numberOfAnnotations )
						return null;
	
					// need to resize
					AnnotationInfo[] temp = new AnnotationInfo[numberOfAnnotations - numStandardAnnotations ];
					int tmpIndex = 0;
					for (int i = 0; i < numberOfAnnotations; i++)
						if (annos[i] != null)
							temp[tmpIndex ++] = annos[i];
					annos = temp;
				}
			}
			return annos;
		}
		return null;
	}
	static TypeAnnotationInfo[] decodeTypeAnnotations(int offset, boolean runtimeVisible, MethodInfo methodInfo) {
		int numberOfAnnotations = methodInfo.u2At(offset + 6);
		if (numberOfAnnotations > 0) {
			int readOffset = offset + 8;
			TypeAnnotationInfo[] typeAnnos = new TypeAnnotationInfo[numberOfAnnotations];
			for (int i = 0; i < numberOfAnnotations; i++) {
				TypeAnnotationInfo newInfo = new TypeAnnotationInfo(methodInfo.reference, methodInfo.constantPoolOffsets, readOffset + methodInfo.structOffset, runtimeVisible, false);
				readOffset += newInfo.readOffset;
				typeAnnos[i] = newInfo;
			}
			return typeAnnos;
		}
		return null;
	}
	static AnnotationInfo[][] decodeParamAnnotations(int offset, boolean runtimeVisible, MethodInfo methodInfo) {
		AnnotationInfo[][] allParamAnnotations = null;
		int numberOfParameters = methodInfo.u1At(offset + 6);
		if (numberOfParameters > 0) {
			// u2 attribute_name_index + u4 attribute_length + u1 num_parameters
			int readOffset = offset + 7;
			for (int i=0 ; i < numberOfParameters; i++) {
				int numberOfAnnotations = methodInfo.u2At(readOffset);
				readOffset += 2;
				if (numberOfAnnotations > 0) {
					if (allParamAnnotations == null)
						allParamAnnotations = new AnnotationInfo[numberOfParameters][];
					AnnotationInfo[] annos = decodeAnnotations(readOffset, runtimeVisible, numberOfAnnotations, methodInfo);
					allParamAnnotations[i] = annos;
					for (int aIndex = 0; aIndex < annos.length; aIndex++)
						readOffset += annos[aIndex].readOffset;
				}
			}
		}
		return allParamAnnotations;
	}
	
	/**
	 * @param classFileBytes byte[]
	 * @param offsets int[]
	 * @param offset int
	 */
	protected MethodInfo (byte classFileBytes[], int offsets[], int offset) {
		super(classFileBytes, offsets, offset);
		this.accessFlags = -1;
		this.signatureUtf8Offset = -1;
	}
	public int compareTo(Object o) {
		MethodInfo otherMethod = (MethodInfo) o;
		int result = new String(getSelector()).compareTo(new String(otherMethod.getSelector()));
		if (result != 0) return result;
		return new String(getMethodDescriptor()).compareTo(new String(otherMethod.getMethodDescriptor()));
	}
	public boolean equals(Object o) {
		if (!(o instanceof MethodInfo)) {
			return false;
		}
		MethodInfo otherMethod = (MethodInfo) o;
		return CharOperation.equals(getSelector(), otherMethod.getSelector())
				&& CharOperation.equals(getMethodDescriptor(), otherMethod.getMethodDescriptor());
	}
	public int hashCode() {
		return CharOperation.hashCode(getSelector()) + CharOperation.hashCode(getMethodDescriptor());
	}
	/**
	 * @see org.summer.sdt.internal.compiler.env.IGenericMethod#getArgumentNames()
	 */
	public char[][] getArgumentNames() {
		if (this.argumentNames == null) {
			readCodeAttribute();
		}
		return this.argumentNames;
	}
	public Object getDefaultValue() {
		return null;
	}
	/**
	 * Answer the resolved names of the exception types in the
	 * class file format as specified in section 4.2 of the Java 2 VM spec
	 * or null if the array is empty.
	 *
	 * For example, java.lang.String is java/lang/String.
	 * @return char[][]
	 */
	public char[][] getExceptionTypeNames() {
		if (this.exceptionNames == null) {
			readExceptionAttributes();
		}
		return this.exceptionNames;
	}
//	public char[] getGenericSignature() {
//		if (this.signatureUtf8Offset != -1) {
//			if (this.signature == null) {
//				// decode the signature
//				this.signature = utf8At(this.signatureUtf8Offset + 3, u2At(this.signatureUtf8Offset + 1));
//			}
//			return this.signature;
//		}
//		return null;
//	}
	/**
	 * Answer the receiver's method descriptor which describes the parameter &
	 * return types as specified in section 4.3.3 of the Java 2 VM spec.
	 *
	 * For example:
	 *   - int foo(String) is (Ljava/lang/String;)I
	 *   - void foo(Object[]) is (I)[Ljava/lang/Object;
	 * @return char[]
	 */
	public char[] getMethodDescriptor() {
		if (this.descriptor == null) {
			// read the name
			int utf8Offset = this.constantPoolOffsets[u2At(4)] - this.structOffset;
			this.descriptor = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
		}
		return this.descriptor;
	}
	/**
	 * Answer an int whose bits are set according the access constants
	 * defined by the VM spec.
	 * Set the AccDeprecated and AccSynthetic bits if necessary
	 * @return int
	 */
//	public int getModifiers() {
//		if (this.accessFlags == -1) {
//			// compute the accessflag. Don't forget the deprecated attribute
//			this.accessFlags = u2At(0);
//			readModifierRelatedAttributes();
//		}
//		return this.accessFlags;
//	}
	public IBinaryAnnotation[] getParameterAnnotations(int index) {
		return null;
	}
	public int getAnnotatedParametersCount() {
		return 0;
	}
//	public IBinaryTypeAnnotation[] getTypeAnnotations() {
//		return null;
//	}
	/**
	 * Answer the name of the method.
	 *
	 * For a constructor, answer <init> & <clinit> for a clinit method.
	 * @return char[]
	 */
	public char[] getSelector() {
		if (this.name == null) {
			// read the name
			int utf8Offset = this.constantPoolOffsets[u2At(2)] - this.structOffset;
			this.name = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
		}
		return this.name;
	}
//	public long getTagBits() {
//		return this.tagBits;
//	}
	/**
	 * This method is used to fully initialize the contents of the receiver. All methodinfos, fields infos
	 * will be therefore fully initialized and we can get rid of the bytes.
	 */
	protected void initialize() {
		getModifiers();
		getSelector();
		getMethodDescriptor();
		getExceptionTypeNames();
		getGenericSignature();
		getArgumentNames();
		reset();
	}
	/**
	 * Answer true if the method is a class initializer, false otherwise.
	 * @return boolean
	 */
	public boolean isClinit() {
		char[] selector = getSelector();
		return selector[0] == '<' && selector.length == 8; // Can only match <clinit>
	}
	/**
	 * Answer true if the method is a constructor, false otherwise.
	 * @return boolean
	 */
	public boolean isConstructor() {
		char[] selector = getSelector();
		return selector[0] == '<' && selector.length == 6; // Can only match <init>
	}
	/**
	 * Return true if the field is a synthetic method, false otherwise.
	 * @return boolean
	 */
//	public boolean isSynthetic() {
//		return (getModifiers() & ClassFileConstants.AccSynthetic) != 0;
//	}
	private void readExceptionAttributes() {
		int attributesCount = u2At(6);
		int readOffset = 8;
		for (int i = 0; i < attributesCount; i++) {
			int utf8Offset = this.constantPoolOffsets[u2At(readOffset)] - this.structOffset;
			char[] attributeName = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
			if (CharOperation.equals(attributeName, AttributeNamesConstants.ExceptionsName)) {
				// read the number of exception entries
				int entriesNumber = u2At(readOffset + 6);
				// place the readOffset at the beginning of the exceptions table
				readOffset += 8;
				if (entriesNumber == 0) {
					this.exceptionNames = noException;
				} else {
					this.exceptionNames = new char[entriesNumber][];
					for (int j = 0; j < entriesNumber; j++) {
						utf8Offset =
							this.constantPoolOffsets[u2At(
								this.constantPoolOffsets[u2At(readOffset)] - this.structOffset + 1)]
								- this.structOffset;
						this.exceptionNames[j] = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
						readOffset += 2;
					}
				}
			} else {
				readOffset += (6 + u4At(readOffset + 2));
			}
		}
		if (this.exceptionNames == null) {
			this.exceptionNames = noException;
		}
	}
	private void readModifierRelatedAttributes() {
		int attributesCount = u2At(6);
		int readOffset = 8;
		for (int i = 0; i < attributesCount; i++) {
			int utf8Offset = this.constantPoolOffsets[u2At(readOffset)] - this.structOffset;
			char[] attributeName = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
			// test added for obfuscated .class file. See 79772
			if (attributeName.length != 0) {
				switch(attributeName[0]) {
					case 'D' :
						if (CharOperation.equals(attributeName, AttributeNamesConstants.DeprecatedName))
							this.accessFlags |= ClassFileConstants.AccDeprecated;
						break;
					case 'S' :
						if (CharOperation.equals(attributeName, AttributeNamesConstants.SyntheticName))
							this.accessFlags |= ClassFileConstants.AccSynthetic;
						break;
					case 'A' :
						if (CharOperation.equals(attributeName, AttributeNamesConstants.AnnotationDefaultName))
							this.accessFlags |= ClassFileConstants.AccAnnotationDefault;
						break;
					case 'V' :
						if (CharOperation.equals(attributeName, AttributeNamesConstants.VarargsName))
							this.accessFlags |= ClassFileConstants.AccVarargs;
				}
			}
			readOffset += (6 + u4At(readOffset + 2));
		}
	}
	/**
	 * Answer the size of the receiver in bytes.
	 *
	 * @return int
	 */
//	public int sizeInBytes() {
//		return this.attributeBytes;
//	}
	
//	public String toString() {
//		StringBuffer buffer = new StringBuffer();
//		toString(buffer);
//		return buffer.toString();
//	}
	void toString(StringBuffer buffer) {
		buffer.append(getClass().getName());
		toStringContent(buffer);
	}
	protected void toStringContent(StringBuffer buffer) {
		int modifiers = getModifiers();
		char[] desc = getGenericSignature();
		if (desc == null)
			desc = getMethodDescriptor();
		buffer
		.append('{')
		.append(
			((modifiers & ClassFileConstants.AccDeprecated) != 0 ? "deprecated " : Util.EMPTY_STRING) //$NON-NLS-1$
				+ ((modifiers & 0x0001) == 1 ? "public " : Util.EMPTY_STRING) //$NON-NLS-1$
				+ ((modifiers & 0x0002) == 0x0002 ? "private " : Util.EMPTY_STRING) //$NON-NLS-1$
				+ ((modifiers & 0x0004) == 0x0004 ? "protected " : Util.EMPTY_STRING) //$NON-NLS-1$
				+ ((modifiers & 0x0008) == 0x000008 ? "static " : Util.EMPTY_STRING) //$NON-NLS-1$
				+ ((modifiers & 0x0010) == 0x0010 ? "final " : Util.EMPTY_STRING) //$NON-NLS-1$
				+ ((modifiers & 0x0040) == 0x0040 ? "bridge " : Util.EMPTY_STRING) //$NON-NLS-1$
				+ ((modifiers & 0x0080) == 0x0080 ? "varargs " : Util.EMPTY_STRING)) //$NON-NLS-1$
		.append(getSelector())
		.append(desc)
		.append('}');
	}
	private void readCodeAttribute() {
		int attributesCount = u2At(6);
		int readOffset = 8;
		if (attributesCount != 0) {
			for (int i = 0; i < attributesCount; i++) {
				int utf8Offset = this.constantPoolOffsets[u2At(readOffset)] - this.structOffset;
				char[] attributeName = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
				if (CharOperation.equals(attributeName, AttributeNamesConstants.CodeName)) {
					decodeCodeAttribute(readOffset);
					if (this.argumentNames == null) {
						this.argumentNames = noArgumentNames;
					}
					return;
				} else {
					readOffset += (6 + u4At(readOffset + 2));
				}
			}
		}
		this.argumentNames = noArgumentNames;
	}
	private void decodeCodeAttribute(int offset) {
		int readOffset = offset + 10;
		int codeLength = (int) u4At(readOffset);
		readOffset += (4 + codeLength);
		int exceptionTableLength = u2At(readOffset);
		readOffset += 2;
		if (exceptionTableLength != 0) {
			for (int i = 0; i < exceptionTableLength; i++) {
				readOffset += 8;
			}
		}
		int attributesCount = u2At(readOffset);
		readOffset += 2;
		for (int i = 0; i < attributesCount; i++) {
			int utf8Offset = this.constantPoolOffsets[u2At(readOffset)] - this.structOffset;
			char[] attributeName = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
			if (CharOperation.equals(attributeName, AttributeNamesConstants.LocalVariableTableName)) {
				decodeLocalVariableAttribute(readOffset, codeLength);
			}
			readOffset += (6 + u4At(readOffset + 2));
		}
	}
	private void decodeLocalVariableAttribute(int offset, int codeLength) {
		int readOffset = offset + 6;
		final int length = u2At(readOffset);
		if (length != 0) {
			readOffset += 2;
			this.argumentNames = new char[length][];
			int argumentNamesIndex = 0;
			for (int i = 0; i < length; i++) {
				int startPC = u2At(readOffset);
				if (startPC == 0) {
					int nameIndex = u2At(4 + readOffset);
					int utf8Offset = this.constantPoolOffsets[nameIndex] - this.structOffset;
					char[] localVariableName = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
					if (!CharOperation.equals(localVariableName, ConstantPool.This)) {
						this.argumentNames[argumentNamesIndex++] = localVariableName;
					}
				} else {
					break;
				}
				readOffset += 10;
			}
			if (argumentNamesIndex != this.argumentNames.length) {
				// resize
				System.arraycopy(this.argumentNames, 0, (this.argumentNames = new char[argumentNamesIndex][]), 0, argumentNamesIndex);
			}
		}
	}
	private void decodeMethodParameters(int offset, MethodInfo methodInfo) {
		int readOffset = offset + 6;
		final int length = u1At(readOffset);
		if (length != 0) {
			readOffset += 1;
			this.argumentNames = new char[length][];
			for (int i = 0; i < length; i++) {
				int nameIndex = u2At(readOffset);
				if (nameIndex != 0) {
					int utf8Offset = this.constantPoolOffsets[nameIndex] - this.structOffset;
					char[] parameterName = utf8At(utf8Offset + 3, u2At(utf8Offset + 1));
					this.argumentNames[i] = parameterName;
				} else {
					this.argumentNames[i] = CharOperation.concat(ARG, String.valueOf(i).toCharArray());
				}
				readOffset += 4;
			}
		}
	}

}
