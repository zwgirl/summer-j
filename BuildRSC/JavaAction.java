// This method is part of an automatic generation : do NOT edit-modify
protected void consumeRule(int act) {
  switch ( act ) {
    case 34 : if (DEBUG) { System.out.println("Type ::= PrimitiveType"); }  //$NON-NLS-1$
		    consumePrimitiveType();  
			break;
 
    case 48 : if (DEBUG) { System.out.println("ReferenceType ::= ClassOrInterfaceType"); }  //$NON-NLS-1$
		    consumeReferenceType();  
			break;
 
    case 52 : if (DEBUG) { System.out.println("ClassOrInterface ::= Name"); }  //$NON-NLS-1$
		    consumeClassOrInterfaceName();  
			break;
 
    case 53 : if (DEBUG) { System.out.println("ClassOrInterface ::= GenericType DOT Name"); }  //$NON-NLS-1$
		    consumeClassOrInterface();  
			break;
 
    case 54 : if (DEBUG) { System.out.println("GenericType ::= ClassOrInterface TypeArguments"); }  //$NON-NLS-1$
		    consumeGenericType();  
			break;
 
    case 55 : if (DEBUG) { System.out.println("GenericType ::= ClassOrInterface LESS GREATER"); }  //$NON-NLS-1$
		    consumeGenericTypeWithDiamond();  
			break;
 
    case 56 : if (DEBUG) { System.out.println("ArrayTypeWithTypeArgumentsName ::= GenericType DOT Name"); }  //$NON-NLS-1$
		    consumeArrayTypeWithTypeArgumentsName();  
			break;
 
    case 57 : if (DEBUG) { System.out.println("ArrayType ::= PrimitiveType Dims"); }  //$NON-NLS-1$
		    consumePrimitiveArrayType();  
			break;
 
    case 58 : if (DEBUG) { System.out.println("ArrayType ::= Name Dims"); }  //$NON-NLS-1$
		    consumeNameArrayType();  
			break;
 
    case 59 : if (DEBUG) { System.out.println("ArrayType ::= ArrayTypeWithTypeArgumentsName Dims"); }  //$NON-NLS-1$
		    consumeGenericTypeNameArrayType();  
			break;
 
    case 60 : if (DEBUG) { System.out.println("ArrayType ::= GenericType Dims"); }  //$NON-NLS-1$
		    consumeGenericTypeArrayType();  
			break;
 
    case 62 : if (DEBUG) { System.out.println("Name ::= SimpleName"); }  //$NON-NLS-1$
		    consumeZeroTypeAnnotations();  
			break;
 
    case 70 : if (DEBUG) { System.out.println("UnannotatableName ::= UnannotatableName DOT SimpleName"); }  //$NON-NLS-1$
		    consumeUnannotatableQualifiedName();  
			break;
 
    case 71 : if (DEBUG) { System.out.println("QualifiedName ::= Name DOT SimpleName"); }  //$NON-NLS-1$
		    consumeQualifiedName(false);  
			break;
 
    case 72 : if (DEBUG) { System.out.println("VariableDeclaratorIdOrThis ::= this"); }  //$NON-NLS-1$
		    consumeExplicitThisParameter(false);  
			break;
 
    case 73 : if (DEBUG) { System.out.println("VariableDeclaratorIdOrThis ::= UnannotatableName DOT this"); }  //$NON-NLS-1$
		    consumeExplicitThisParameter(true);  
			break;
 
    case 74 : if (DEBUG) { System.out.println("VariableDeclaratorIdOrThis ::= VariableDeclaratorId"); }  //$NON-NLS-1$
		    consumeVariableDeclaratorIdParameter();  
			break;
 
    case 75 : if (DEBUG) { System.out.println("CompilationUnit ::= EnterCompilationUnit..."); }  //$NON-NLS-1$
		    consumeCompilationUnit();  
			break;
 
    case 76 : if (DEBUG) { System.out.println("InternalCompilationUnit ::= PackageDeclaration"); }  //$NON-NLS-1$
		    consumeInternalCompilationUnit();  
			break;
 
    case 77 : if (DEBUG) { System.out.println("InternalCompilationUnit ::= PackageDeclaration..."); }  //$NON-NLS-1$
		    consumeInternalCompilationUnit();  
			break;
 
    case 78 : if (DEBUG) { System.out.println("InternalCompilationUnit ::= PackageDeclaration..."); }  //$NON-NLS-1$
		    consumeInternalCompilationUnitWithTypes();  
			break;
 
    case 79 : if (DEBUG) { System.out.println("InternalCompilationUnit ::= PackageDeclaration..."); }  //$NON-NLS-1$
		    consumeInternalCompilationUnitWithTypes();  
			break;
 
    case 80 : if (DEBUG) { System.out.println("InternalCompilationUnit ::= ImportDeclarations..."); }  //$NON-NLS-1$
		    consumeInternalCompilationUnit();  
			break;
 
    case 81 : if (DEBUG) { System.out.println("InternalCompilationUnit ::= TypeDeclarations"); }  //$NON-NLS-1$
		    consumeInternalCompilationUnitWithTypes();  
			break;
 
    case 82 : if (DEBUG) { System.out.println("InternalCompilationUnit ::= ImportDeclarations..."); }  //$NON-NLS-1$
		    consumeInternalCompilationUnitWithTypes();  
			break;
 
    case 83 : if (DEBUG) { System.out.println("InternalCompilationUnit ::="); }  //$NON-NLS-1$
		    consumeEmptyInternalCompilationUnit();  
			break;
 
    case 87 : if (DEBUG) { System.out.println("ElementListopt ::="); }  //$NON-NLS-1$
		    consumeElementListopt();  
			break;
 
    case 90 : if (DEBUG) { System.out.println("ElementList ::= ElementList Element"); }  //$NON-NLS-1$
		    consumeElementList();  
			break;
 
    case 91 : if (DEBUG) { System.out.println("PCDATAElement ::= PCDATA"); }  //$NON-NLS-1$
		    consumePCDATAElement();  
			break;
 
    case 92 : if (DEBUG) { System.out.println("ObjectElement ::= ElementTag EnterPCADATATag CLOSE_TAG"); }  //$NON-NLS-1$
		    consumeEmptyObjectElement();  
			break;
 
    case 93 : if (DEBUG) { System.out.println("ObjectElement ::= ElementTag AttributeList..."); }  //$NON-NLS-1$
		    consumeObjectElementNoChild();  
			break;
 
    case 94 : if (DEBUG) { System.out.println("ObjectElement ::= ElementTag AttributeListopt..."); }  //$NON-NLS-1$
		    consumeObjectElement();  
			break;
 
    case 95 : if (DEBUG) { System.out.println("RootObjectElement ::= ElementTag AttributeListopt..."); }  //$NON-NLS-1$
		    consumeRootObjectElement();  
			break;
 
    case 96 : if (DEBUG) { System.out.println("EnterPCADATATag ::="); }  //$NON-NLS-1$
		    consumeEnterPCADATATag();  
			break;
 
    case 97 : if (DEBUG) { System.out.println("AttributeListopt ::="); }  //$NON-NLS-1$
		    consumeAttributeListopt();  
			break;
 
    case 99 : if (DEBUG) { System.out.println("AttributeElementTag ::="); }  //$NON-NLS-1$
		    consumeAttributeElementTag();  
			break;
 
    case 100 : if (DEBUG) { System.out.println("ElementTag ::= LESS SimpleName"); }  //$NON-NLS-1$
		    consumeElementTag();  
			break;
 
    case 101 : if (DEBUG) { System.out.println("AttributeElement ::= LESS SimpleName DOT SimpleName..."); }  //$NON-NLS-1$
		    consumeAttributeElement();  
			break;
 
    case 103 : if (DEBUG) { System.out.println("AttributeList ::= AttributeList Attribute"); }  //$NON-NLS-1$
		    consumeAttributeAttributeList();  
			break;
 
    case 106 : if (DEBUG) { System.out.println("AttachAttribute ::= SimpleName DOT SimpleName EQUAL..."); }  //$NON-NLS-1$
		    consumeAttachAttribute();  
			break;
 
    case 107 : if (DEBUG) { System.out.println("GeneralAttribute ::= SimpleName EQUAL PropertyExpression"); }  //$NON-NLS-1$
		    consumeGeneralAttribute();  
			break;
 
    case 110 : if (DEBUG) { System.out.println("MarkupExtenson ::= LBRACE SimpleName AttributeList..."); }  //$NON-NLS-1$
		    consumeMarkupExtenson();  
			break;
 
    case 111 : if (DEBUG) { System.out.println("ReduceImports ::="); }  //$NON-NLS-1$
		    consumeReduceImports();  
			break;
 
    case 112 : if (DEBUG) { System.out.println("EnterCompilationUnit ::="); }  //$NON-NLS-1$
		    consumeEnterCompilationUnit();  
			break;
 
    case 127 : if (DEBUG) { System.out.println("CatchHeader ::= catch LPAREN CatchFormalParameter RPAREN"); }  //$NON-NLS-1$
		    consumeCatchHeader();  
			break;
 
    case 129 : if (DEBUG) { System.out.println("ImportDeclarations ::= ImportDeclarations..."); }  //$NON-NLS-1$
		    consumeImportDeclarations();  
			break;
 
    case 131 : if (DEBUG) { System.out.println("TypeDeclarations ::= TypeDeclarations TypeDeclaration"); }  //$NON-NLS-1$
		    consumeTypeDeclarations();  
			break;
 
    case 132 : if (DEBUG) { System.out.println("PackageDeclaration ::= PackageDeclarationName SEMICOLON"); }  //$NON-NLS-1$
		    consumePackageDeclaration();  
			break;
 
    case 133 : if (DEBUG) { System.out.println("PackageDeclarationName ::= Modifiers package..."); }  //$NON-NLS-1$
		    consumePackageDeclarationNameWithModifiers();  
			break;
 
    case 134 : if (DEBUG) { System.out.println("PackageDeclarationName ::= Modifiers module..."); }  //$NON-NLS-1$
		    consumeModuleDeclarationNameWithModifiers();  
			break;
 
    case 135 : if (DEBUG) { System.out.println("PackageDeclarationName ::= PackageComment package Name"); }  //$NON-NLS-1$
		    consumePackageDeclarationName();  
			break;
 
    case 136 : if (DEBUG) { System.out.println("PackageDeclarationName ::= PackageComment module Name"); }  //$NON-NLS-1$
		    consumeModuleDeclarationName();  
			break;
 
    case 137 : if (DEBUG) { System.out.println("PackageComment ::="); }  //$NON-NLS-1$
		    consumePackageComment();  
			break;
 
    case 142 : if (DEBUG) { System.out.println("SingleTypeImportDeclaration ::=..."); }  //$NON-NLS-1$
		    consumeImportDeclaration();  
			break;
 
    case 143 : if (DEBUG) { System.out.println("SingleTypeImportDeclarationName ::= import Name"); }  //$NON-NLS-1$
		    consumeSingleTypeImportDeclarationName();  
			break;
 
    case 144 : if (DEBUG) { System.out.println("TypeImportOnDemandDeclaration ::=..."); }  //$NON-NLS-1$
		    consumeImportDeclaration();  
			break;
 
    case 145 : if (DEBUG) { System.out.println("TypeImportOnDemandDeclarationName ::= import Name DOT..."); }  //$NON-NLS-1$
		    consumeTypeImportOnDemandDeclarationName();  
			break;
 
     case 148 : if (DEBUG) { System.out.println("TypeDeclaration ::= SEMICOLON"); }  //$NON-NLS-1$
		    consumeEmptyTypeDeclaration();  
			break;
 
    case 153 : if (DEBUG) { System.out.println("Modifiers ::= Modifiers Modifier"); }  //$NON-NLS-1$
		    consumeModifiers2();  
			break;
 
    case 164 : if (DEBUG) { System.out.println("Modifier ::= Annotation"); }  //$NON-NLS-1$
		    consumeAnnotationAsModifier();  
			break;
 
    case 165 : if (DEBUG) { System.out.println("FunctionType ::= Modifiersopt function Type add..."); }  //$NON-NLS-1$
		    consumeFunctionTypeWithTypeParameters();  
			break;
 
    case 166 : if (DEBUG) { System.out.println("FunctionType ::= Modifiersopt function Type remove..."); }  //$NON-NLS-1$
		    consumeFunctionTypeWithTypeParameters();  
			break;
 
    case 167 : if (DEBUG) { System.out.println("FunctionType ::= Modifiersopt function Type get..."); }  //$NON-NLS-1$
		    consumeFunctionTypeWithTypeParameters();  
			break;
 
    case 168 : if (DEBUG) { System.out.println("FunctionType ::= Modifiersopt function Type set..."); }  //$NON-NLS-1$
		    consumeFunctionTypeWithTypeParameters();  
			break;
 
    case 169 : if (DEBUG) { System.out.println("FunctionType ::= Modifiersopt function Type Identifier"); }  //$NON-NLS-1$
		    consumeFunctionTypeWithTypeParameters();  
			break;
 
    case 170 : if (DEBUG) { System.out.println("FunctionType ::= Modifiersopt function Type add LPAREN"); }  //$NON-NLS-1$
		    consumeFunctionType();  
			break;
 
    case 171 : if (DEBUG) { System.out.println("FunctionType ::= Modifiersopt function Type remove..."); }  //$NON-NLS-1$
		    consumeFunctionType();  
			break;
 
    case 172 : if (DEBUG) { System.out.println("FunctionType ::= Modifiersopt function Type get LPAREN"); }  //$NON-NLS-1$
		    consumeFunctionType();  
			break;
 
    case 173 : if (DEBUG) { System.out.println("FunctionType ::= Modifiersopt function Type set LPAREN"); }  //$NON-NLS-1$
		    consumeFunctionType();  
			break;
 
    case 174 : if (DEBUG) { System.out.println("FunctionType ::= Modifiersopt function Type Identifier"); }  //$NON-NLS-1$
		    consumeFunctionType();  
			break;
 
    case 175 : if (DEBUG) { System.out.println("ClassDeclaration ::= ClassHeader ClassBody"); }  //$NON-NLS-1$
		    consumeClassDeclaration();  
			break;
 
    case 176 : if (DEBUG) { System.out.println("ClassHeader ::= ClassHeaderName ClassHeaderExtendsopt..."); }  //$NON-NLS-1$
		    consumeClassHeader();  
			break;
 
    case 177 : if (DEBUG) { System.out.println("ClassHeaderName ::= ClassHeaderName1 TypeParameters"); }  //$NON-NLS-1$
		    consumeTypeHeaderNameWithTypeParameters();  
			break;
 
    case 183 : if (DEBUG) { System.out.println("ClassHeaderName1 ::= Modifiersopt class Identifier"); }  //$NON-NLS-1$
		    consumeClassHeaderName1();  
			break;
 
    case 184 : if (DEBUG) { System.out.println("ClassHeaderExtends ::= extends ClassType"); }  //$NON-NLS-1$
		    consumeClassHeaderExtends();  
			break;
 
    case 185 : if (DEBUG) { System.out.println("ClassHeaderImplements ::= implements InterfaceTypeList"); }  //$NON-NLS-1$
		    consumeClassHeaderImplements();  
			break;
 
    case 187 : if (DEBUG) { System.out.println("InterfaceTypeList ::= InterfaceTypeList COMMA..."); }  //$NON-NLS-1$
		    consumeInterfaceTypeList();  
			break;
 
    case 188 : if (DEBUG) { System.out.println("InterfaceType ::= ClassOrInterfaceType"); }  //$NON-NLS-1$
		    consumeInterfaceType();  
			break;
 
    case 191 : if (DEBUG) { System.out.println("ClassBodyDeclarations ::= ClassBodyDeclarations..."); }  //$NON-NLS-1$
		    consumeClassBodyDeclarations();  
			break;
 
    case 195 : if (DEBUG) { System.out.println("ClassBodyDeclaration ::= Diet NestedMethod..."); }  //$NON-NLS-1$
		    consumeClassBodyDeclaration();  
			break;
 
    case 196 : if (DEBUG) { System.out.println("Diet ::="); }  //$NON-NLS-1$
		    consumeDiet();  
			break;

    case 197 : if (DEBUG) { System.out.println("Initializer ::= Diet NestedMethod CreateInitializer..."); }  //$NON-NLS-1$
		    consumeClassBodyDeclaration();  
			break;
 
    case 198 : if (DEBUG) { System.out.println("CreateInitializer ::="); }  //$NON-NLS-1$
		    consumeCreateInitializer();  
			break;

    case 200 : if (DEBUG) { System.out.println("PropertyDeclaration ::= Modifiersopt Type Identifier..."); }  //$NON-NLS-1$
		    consumePropertyDeclaration();  
			break;

    case 201 : if (DEBUG) { System.out.println("PropertyHeader ::="); }  //$NON-NLS-1$
		    consumePropertyHeader();  
			break;

    case 202 : if (DEBUG) { System.out.println("AccessorDeclaration ::="); }  //$NON-NLS-1$
		    consumeEmptyAccessor();  
			break;

    case 206 : if (DEBUG) { System.out.println("SetAccessorDeclarationopt ::= SetAccessorDeclaration"); }  //$NON-NLS-1$
		    consumeAccessoropt();  
			break;

    case 207 : if (DEBUG) { System.out.println("SetAccessorDeclaration ::= SetterMethodHeader MethodBody"); }  //$NON-NLS-1$
		     consumeMethodDeclaration(true, false);   
			break;

    case 208 : if (DEBUG) { System.out.println("SetAccessorDeclaration ::= SetterMethodHeader SEMICOLON"); }  //$NON-NLS-1$
		     consumeMethodDeclaration(false, false);   
			break;

    case 209 : if (DEBUG) { System.out.println("SetterMethodHeader ::= set"); }  //$NON-NLS-1$
		    consumeAccessorMethodName(MethodDeclaration.SETTER);  
			break;

    case 211 : if (DEBUG) { System.out.println("GetAccessorDeclarationopt ::= GetAccessorDeclaration"); }  //$NON-NLS-1$
		    consumeAccessoropt();  
			break;

    case 212 : if (DEBUG) { System.out.println("GetAccessorDeclaration ::= GetterMethodHeader MethodBody"); }  //$NON-NLS-1$
		     consumeMethodDeclaration(true, false);   
			break;

    case 213 : if (DEBUG) { System.out.println("GetAccessorDeclaration ::= GetterMethodHeader SEMICOLON"); }  //$NON-NLS-1$
		     consumeMethodDeclaration(false, false);   
			break;

    case 214 : if (DEBUG) { System.out.println("GetterMethodHeader ::= get"); }  //$NON-NLS-1$
		    consumeAccessorMethodName(MethodDeclaration.GETTER);  
			break;

    case 216 : if (DEBUG) { System.out.println("EventDeclaration ::= EventHeader LBRACE..."); }  //$NON-NLS-1$
		    consumeEventDeclaration();  
			break;

    case 217 : if (DEBUG) { System.out.println("EventDeclaration ::= EventHeader SEMICOLON"); }  //$NON-NLS-1$
		    consumeEventDeclarationNoAccessor();  
			break;

    case 218 : if (DEBUG) { System.out.println("EventHeader ::= Modifiersopt event Type Identifier"); }  //$NON-NLS-1$
		    consumeEventHeader();  
			break;

    case 219 : if (DEBUG) { System.out.println("EventAccessorDeclaration ::="); }  //$NON-NLS-1$
		    consumeEmptyAccessor();  
			break;

    case 223 : if (DEBUG) { System.out.println("AddAccessorDeclarationopt ::= AddAccessorDeclaration"); }  //$NON-NLS-1$
		    consumeAccessoropt();  
			break;

    case 224 : if (DEBUG) { System.out.println("AddAccessorDeclaration ::= AddMethodHeader MethodBody"); }  //$NON-NLS-1$
		    consumeMethodDeclaration(true, false);  
			break;

    case 225 : if (DEBUG) { System.out.println("AddMethodHeader ::= add"); }  //$NON-NLS-1$
		    consumeAccessorMethodName(MethodDeclaration.ADD);  
			break;

    case 227 : if (DEBUG) { System.out.println("RemoveAccessorDeclarationopt ::=..."); }  //$NON-NLS-1$
		    consumeAccessoropt();  
			break;

    case 228 : if (DEBUG) { System.out.println("RemoveAccessorDeclaration ::= RemoveMethodHeader..."); }  //$NON-NLS-1$
		    consumeMethodDeclaration(true, false);  
			break;

    case 229 : if (DEBUG) { System.out.println("RemoveMethodHeader ::= remove"); }  //$NON-NLS-1$
		    consumeAccessorMethodName(MethodDeclaration.REMOVE);  
			break;

    case 231 : if (DEBUG) { System.out.println("IndexerDeclaration ::= IndexerHeader FormalParameter..."); }  //$NON-NLS-1$
		    consumeIndexerDeclaration();  
			break;

    case 232 : if (DEBUG) { System.out.println("IndexerDeclaration ::= IndexerHeader FormalParameter..."); }  //$NON-NLS-1$
		    consumeIndexerDeclarationNoAccessor();  
			break;

    case 233 : if (DEBUG) { System.out.println("IndexerHeader ::= Modifiersopt Type this LBRACKET"); }  //$NON-NLS-1$
		    consumeIndexerHeader();  
			break;

    case 242 : if (DEBUG) { System.out.println("ClassMemberDeclaration ::= SEMICOLON"); }  //$NON-NLS-1$
		    consumeEmptyTypeDeclaration();  
			break;

    case 245 : if (DEBUG) { System.out.println("FieldDeclaration ::= Modifiersopt Type..."); }  //$NON-NLS-1$
		    consumeFieldDeclaration();  
			break;
 
    case 247 : if (DEBUG) { System.out.println("VariableDeclarators ::= VariableDeclarators COMMA..."); }  //$NON-NLS-1$
		    consumeVariableDeclarators();  
			break;
 
    case 250 : if (DEBUG) { System.out.println("EnterVariable ::="); }  //$NON-NLS-1$
		    consumeEnterVariable();  
			break;
 
    case 251 : if (DEBUG) { System.out.println("ExitVariableWithInitialization ::="); }  //$NON-NLS-1$
		    consumeExitVariableWithInitialization();  
			break;
 
    case 252 : if (DEBUG) { System.out.println("ExitVariableWithoutInitialization ::="); }  //$NON-NLS-1$
		    consumeExitVariableWithoutInitialization();  
			break;
 
    case 253 : if (DEBUG) { System.out.println("ForceNoDiet ::="); }  //$NON-NLS-1$
		    consumeForceNoDiet();  
			break;
 
    case 254 : if (DEBUG) { System.out.println("RestoreDiet ::="); }  //$NON-NLS-1$
		    consumeRestoreDiet();  
			break;
 
    case 263 : if (DEBUG) { System.out.println("MethodDeclaration ::= MethodHeader MethodBody"); }  //$NON-NLS-1$
		    // set to true to consume a method with a body
 consumeMethodDeclaration(true, false);  
			break;
 
    case 264 : if (DEBUG) { System.out.println("MethodDeclaration ::= DefaultMethodHeader MethodBody"); }  //$NON-NLS-1$
		    // set to true to consume a method with a body
 consumeMethodDeclaration(true, true);  
			break;
 
    case 265 : if (DEBUG) { System.out.println("AbstractMethodDeclaration ::= MethodHeader SEMICOLON"); }  //$NON-NLS-1$
		    // set to false to consume a method without body
 consumeMethodDeclaration(false, false);  
			break;
 
    case 266 : if (DEBUG) { System.out.println("MethodHeader ::= MethodHeaderName FormalParameterListopt"); }  //$NON-NLS-1$
		    consumeMethodHeader();  
			break;
 
    case 267 : if (DEBUG) { System.out.println("DefaultMethodHeader ::= DefaultMethodHeaderName..."); }  //$NON-NLS-1$
		    consumeMethodHeader();  
			break;
 
    case 268 : if (DEBUG) { System.out.println("MethodHeaderName ::= Modifiersopt Type add..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(false);  
			break;
 
    case 269 : if (DEBUG) { System.out.println("MethodHeaderName ::= Modifiersopt Type remove..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(false);  
			break;
 
    case 270 : if (DEBUG) { System.out.println("MethodHeaderName ::= Modifiersopt Type get..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(false);  
			break;
 
    case 271 : if (DEBUG) { System.out.println("MethodHeaderName ::= Modifiersopt Type set..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(false);  
			break;
 
    case 272 : if (DEBUG) { System.out.println("MethodHeaderName ::= Modifiersopt Type Identifier..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(false);  
			break;
 
    case 273 : if (DEBUG) { System.out.println("MethodHeaderName ::= Modifiersopt Type add LPAREN"); }  //$NON-NLS-1$
		    consumeMethodHeaderName(false);  
			break;
 
    case 274 : if (DEBUG) { System.out.println("MethodHeaderName ::= Modifiersopt Type remove LPAREN"); }  //$NON-NLS-1$
		    consumeMethodHeaderName(false);  
			break;
 
    case 275 : if (DEBUG) { System.out.println("MethodHeaderName ::= Modifiersopt Type get LPAREN"); }  //$NON-NLS-1$
		    consumeMethodHeaderName(false);  
			break;
 
    case 276 : if (DEBUG) { System.out.println("MethodHeaderName ::= Modifiersopt Type set LPAREN"); }  //$NON-NLS-1$
		    consumeMethodHeaderName(false);  
			break;
 
    case 277 : if (DEBUG) { System.out.println("MethodHeaderName ::= Modifiersopt Type Identifier LPAREN"); }  //$NON-NLS-1$
		    consumeMethodHeaderName(false);  
			break;
 
    case 278 : if (DEBUG) { System.out.println("DefaultMethodHeaderName ::= ModifiersWithDefault..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(false);  
			break;
 
    case 279 : if (DEBUG) { System.out.println("DefaultMethodHeaderName ::= ModifiersWithDefault..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(false);  
			break;
 
    case 280 : if (DEBUG) { System.out.println("DefaultMethodHeaderName ::= ModifiersWithDefault..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(false);  
			break;
 
    case 281 : if (DEBUG) { System.out.println("DefaultMethodHeaderName ::= ModifiersWithDefault..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(false);  
			break;
 
    case 282 : if (DEBUG) { System.out.println("DefaultMethodHeaderName ::= ModifiersWithDefault..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(false);  
			break;
 
    case 283 : if (DEBUG) { System.out.println("DefaultMethodHeaderName ::= ModifiersWithDefault Type..."); }  //$NON-NLS-1$
		    consumeMethodHeaderName(false);  
			break;
 
    case 284 : if (DEBUG) { System.out.println("DefaultMethodHeaderName ::= ModifiersWithDefault Type..."); }  //$NON-NLS-1$
		    consumeMethodHeaderName(false);  
			break;
 
    case 285 : if (DEBUG) { System.out.println("DefaultMethodHeaderName ::= ModifiersWithDefault Type..."); }  //$NON-NLS-1$
		    consumeMethodHeaderName(false);  
			break;
 
    case 286 : if (DEBUG) { System.out.println("DefaultMethodHeaderName ::= ModifiersWithDefault Type..."); }  //$NON-NLS-1$
		    consumeMethodHeaderName(false);  
			break;
 
    case 287 : if (DEBUG) { System.out.println("DefaultMethodHeaderName ::= ModifiersWithDefault Type..."); }  //$NON-NLS-1$
		    consumeMethodHeaderName(false);  
			break;
 
    case 288 : if (DEBUG) { System.out.println("ModifiersWithDefault ::= Modifiersopt default..."); }  //$NON-NLS-1$
		    consumePushCombineModifiers();  
			break;
 
    case 289 : if (DEBUG) { System.out.println("MethodHeaderRightParen ::= RPAREN"); }  //$NON-NLS-1$
		    consumeMethodHeaderRightParen();  
			break;
 
    case 290 : if (DEBUG) { System.out.println("MethodHeaderExtendedDims ::= Dimsopt"); }  //$NON-NLS-1$
		    consumeMethodHeaderExtendedDims();  
			break;
 
    case 291 : if (DEBUG) { System.out.println("MethodHeaderThrowsClause ::= throws ClassTypeList"); }  //$NON-NLS-1$
		    consumeMethodHeaderThrowsClause();  
			break;
 
    case 292 : if (DEBUG) { System.out.println("ConstructorHeader ::= ConstructorHeaderName..."); }  //$NON-NLS-1$
		    consumeConstructorHeader();  
			break;
 
    case 297 : if (DEBUG) { System.out.println("ConstructorHeaderName ::= Modifiersopt Identifier LPAREN"); }  //$NON-NLS-1$
		    consumeConstructorHeaderName();  
			break;
 
    case 299 : if (DEBUG) { System.out.println("FormalParameterList ::= FormalParameterList COMMA..."); }  //$NON-NLS-1$
		    consumeFormalParameterList();  
			break;
 
    case 300 : if (DEBUG) { System.out.println("FormalParameter ::= Modifiersopt Type..."); }  //$NON-NLS-1$
		    consumeFormalParameter(false);  
			break;
 
    case 301 : if (DEBUG) { System.out.println("FormalParameter ::= Modifiersopt Type ELLIPSIS..."); }  //$NON-NLS-1$
		    consumeFormalParameter(true);  
			break;
 
    case 302 : if (DEBUG) { System.out.println("CatchFormalParameter ::= Modifiersopt CatchType..."); }  //$NON-NLS-1$
		    consumeCatchFormalParameter();  
			break;
 
    case 303 : if (DEBUG) { System.out.println("CatchType ::= UnionType"); }  //$NON-NLS-1$
		    consumeCatchType();  
			break;
 
    case 304 : if (DEBUG) { System.out.println("UnionType ::= Type"); }  //$NON-NLS-1$
		    consumeUnionTypeAsClassType();  
			break;
 
    case 305 : if (DEBUG) { System.out.println("UnionType ::= UnionType OR Type"); }  //$NON-NLS-1$
		    consumeUnionType();  
			break;
 
    case 307 : if (DEBUG) { System.out.println("ClassTypeList ::= ClassTypeList COMMA ClassTypeElt"); }  //$NON-NLS-1$
		    consumeClassTypeList();  
			break;
 
    case 308 : if (DEBUG) { System.out.println("ClassTypeElt ::= ClassType"); }  //$NON-NLS-1$
		    consumeClassTypeElt();  
			break;
 
    case 309 : if (DEBUG) { System.out.println("MethodBody ::= NestedMethod LBRACE BlockStatementsopt..."); }  //$NON-NLS-1$
		    consumeMethodBody();  
			break;
 
    case 310 : if (DEBUG) { System.out.println("NestedMethod ::="); }  //$NON-NLS-1$
		    consumeNestedMethod();  
			break;
 
    case 311 : if (DEBUG) { System.out.println("StaticInitializer ::= StaticOnly Block"); }  //$NON-NLS-1$
		    consumeStaticInitializer();  
			break;

    case 312 : if (DEBUG) { System.out.println("StaticOnly ::= static"); }  //$NON-NLS-1$
		    consumeStaticOnly();  
			break;
 
    case 313 : if (DEBUG) { System.out.println("ConstructorDeclaration ::= ConstructorHeader MethodBody"); }  //$NON-NLS-1$
		    consumeConstructorDeclaration() ;  
			break;
 
    case 314 : if (DEBUG) { System.out.println("ConstructorDeclaration ::= ConstructorHeader SEMICOLON"); }  //$NON-NLS-1$
		    consumeInvalidConstructorDeclaration() ;  
			break;
 
    case 315 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= this LPAREN..."); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocation(0, THIS_CALL);  
			break;
 
    case 316 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= this OnlyTypeArguments"); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocationWithTypeArguments(0,THIS_CALL);  
			break;
 
    case 317 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= super LPAREN..."); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocation(0,SUPER_CALL);  
			break;
 
    case 318 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= super..."); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocationWithTypeArguments(0,SUPER_CALL);  
			break;
 
    case 319 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= Primary DOT super..."); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocation(1, SUPER_CALL);  
			break;
 
    case 320 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= Primary DOT super..."); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocationWithTypeArguments(1, SUPER_CALL);  
			break;
 
    case 321 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= Name DOT super LPAREN"); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocation(2, SUPER_CALL);  
			break;
 
    case 322 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= Name DOT super..."); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocationWithTypeArguments(2, SUPER_CALL);  
			break;
 
    case 323 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= Primary DOT this..."); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocation(1, THIS_CALL);  
			break;
 
    case 324 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= Primary DOT this..."); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocationWithTypeArguments(1, THIS_CALL);  
			break;
 
    case 325 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= Name DOT this LPAREN"); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocation(2, THIS_CALL);  
			break;
 
    case 326 : if (DEBUG) { System.out.println("ExplicitConstructorInvocation ::= Name DOT this..."); }  //$NON-NLS-1$
		    consumeExplicitConstructorInvocationWithTypeArguments(2, THIS_CALL);  
			break;
 
    case 327 : if (DEBUG) { System.out.println("InterfaceDeclaration ::= InterfaceHeader InterfaceBody"); }  //$NON-NLS-1$
		    consumeInterfaceDeclaration();  
			break;
 
    case 328 : if (DEBUG) { System.out.println("InterfaceHeader ::= InterfaceHeaderName..."); }  //$NON-NLS-1$
		    consumeInterfaceHeader();  
			break;
 
    case 329 : if (DEBUG) { System.out.println("InterfaceHeaderName ::= InterfaceHeaderName1..."); }  //$NON-NLS-1$
		    consumeTypeHeaderNameWithTypeParameters();  
			break;
 
    case 335 : if (DEBUG) { System.out.println("InterfaceHeaderName1 ::= Modifiersopt interface..."); }  //$NON-NLS-1$
		    consumeInterfaceHeaderName1();  
			break;
 
    case 336 : if (DEBUG) { System.out.println("InterfaceHeaderExtends ::= extends InterfaceTypeList"); }  //$NON-NLS-1$
		    consumeInterfaceHeaderExtends();  
			break;
 
    case 339 : if (DEBUG) { System.out.println("InterfaceMemberDeclarations ::=..."); }  //$NON-NLS-1$
		    consumeInterfaceMemberDeclarations();  
			break;
 
    case 340 : if (DEBUG) { System.out.println("InterfaceMemberDeclaration ::= SEMICOLON"); }  //$NON-NLS-1$
		    consumeEmptyTypeDeclaration();  
			break;
 
    case 344 : if (DEBUG) { System.out.println("InterfaceMemberDeclaration ::= DefaultMethodHeader..."); }  //$NON-NLS-1$
		    consumeInterfaceMethodDeclaration(false);  
			break;
 
    case 345 : if (DEBUG) { System.out.println("InterfaceMemberDeclaration ::= MethodHeader MethodBody"); }  //$NON-NLS-1$
		    consumeInterfaceMethodDeclaration(false);  
			break;
 
    case 346 : if (DEBUG) { System.out.println("InterfaceMemberDeclaration ::= DefaultMethodHeader..."); }  //$NON-NLS-1$
		    consumeInterfaceMethodDeclaration(true);  
			break;
 
    case 347 : if (DEBUG) { System.out.println("InvalidConstructorDeclaration ::= ConstructorHeader..."); }  //$NON-NLS-1$
		    consumeInvalidConstructorDeclaration(true);  
			break;
 
    case 348 : if (DEBUG) { System.out.println("InvalidConstructorDeclaration ::= ConstructorHeader..."); }  //$NON-NLS-1$
		    consumeInvalidConstructorDeclaration(false);  
			break;
 
    case 359 : if (DEBUG) { System.out.println("PushLeftBrace ::="); }  //$NON-NLS-1$
		    consumePushLeftBrace();  
			break;
 
    case 360 : if (DEBUG) { System.out.println("ArrayInitializer ::= LBRACE PushLeftBrace ,opt RBRACE"); }  //$NON-NLS-1$
		    consumeEmptyArrayInitializer();  
			break;
 
    case 361 : if (DEBUG) { System.out.println("ArrayInitializer ::= LBRACE PushLeftBrace..."); }  //$NON-NLS-1$
		    consumeArrayInitializer();  
			break;
 
    case 362 : if (DEBUG) { System.out.println("ArrayInitializer ::= LBRACE PushLeftBrace..."); }  //$NON-NLS-1$
		    consumeArrayInitializer();  
			break;
 
    case 364 : if (DEBUG) { System.out.println("VariableInitializers ::= VariableInitializers COMMA..."); }  //$NON-NLS-1$
		    consumeVariableInitializers();  
			break;
 
    case 365 : if (DEBUG) { System.out.println("Block ::= OpenBlock LBRACE BlockStatementsopt RBRACE"); }  //$NON-NLS-1$
		    consumeBlock();  
			break;
 
    case 366 : if (DEBUG) { System.out.println("OpenBlock ::="); }  //$NON-NLS-1$
		    consumeOpenBlock() ;  
			break;
 
    case 367 : if (DEBUG) { System.out.println("BlockStatements ::= BlockStatement"); }  //$NON-NLS-1$
		    consumeBlockStatement() ;  
			break;
 
    case 368 : if (DEBUG) { System.out.println("BlockStatements ::= BlockStatements BlockStatement"); }  //$NON-NLS-1$
		    consumeBlockStatements() ;  
			break;
 
    case 375 : if (DEBUG) { System.out.println("FunctionDeclaration ::= FunctionHeaderName..."); }  //$NON-NLS-1$
		    consumeFunctionDeclaration();  
			break;
 
    case 376 : if (DEBUG) { System.out.println("FunctionHeaderName ::= function Type Identifier..."); }  //$NON-NLS-1$
		    consumeFunctionHeaderNameWithTypeParameters();  
			break;
 
    case 377 : if (DEBUG) { System.out.println("FunctionHeaderName ::= function Type Identifier LPAREN"); }  //$NON-NLS-1$
		    consumeFunctionHeaderName();  
			break;
 
    case 379 : if (DEBUG) { System.out.println("BlockStatement ::= InterfaceDeclaration"); }  //$NON-NLS-1$
		    consumeInvalidInterfaceDeclaration();  
			break;
 
    case 380 : if (DEBUG) { System.out.println("BlockStatement ::= AnnotationTypeDeclaration"); }  //$NON-NLS-1$
		    consumeInvalidAnnotationTypeDeclaration();  
			break;
 
    case 381 : if (DEBUG) { System.out.println("BlockStatement ::= EnumDeclaration"); }  //$NON-NLS-1$
		    consumeInvalidEnumDeclaration();  
			break;
 
    case 382 : if (DEBUG) { System.out.println("LocalVariableDeclarationStatement ::=..."); }  //$NON-NLS-1$
		    consumeLocalVariableDeclarationStatement();  
			break;
 
    case 383 : if (DEBUG) { System.out.println("LocalVariableDeclaration ::= Type PushModifiers..."); }  //$NON-NLS-1$
		    consumeLocalVariableDeclaration();  
			break;
 
    case 384 : if (DEBUG) { System.out.println("LocalVariableDeclaration ::= Modifiers Type..."); }  //$NON-NLS-1$
		    consumeLocalVariableDeclaration();  
			break;
 
    case 385 : if (DEBUG) { System.out.println("PushModifiers ::="); }  //$NON-NLS-1$
		    consumePushModifiers();  
			break;
 
    case 386 : if (DEBUG) { System.out.println("PushModifiersForHeader ::="); }  //$NON-NLS-1$
		    consumePushModifiersForHeader();  
			break;
 
    case 387 : if (DEBUG) { System.out.println("PushRealModifiers ::="); }  //$NON-NLS-1$
		    consumePushRealModifiers();  
			break;
 
    case 414 : if (DEBUG) { System.out.println("EmptyStatement ::= SEMICOLON"); }  //$NON-NLS-1$
		    consumeEmptyStatement();  
			break;
 
    case 415 : if (DEBUG) { System.out.println("LabeledStatement ::= Label COLON Statement"); }  //$NON-NLS-1$
		    consumeStatementLabel() ;  
			break;
 
    case 416 : if (DEBUG) { System.out.println("LabeledStatementNoShortIf ::= Label COLON..."); }  //$NON-NLS-1$
		    consumeStatementLabel() ;  
			break;
 
    case 421 : if (DEBUG) { System.out.println("Label ::= Identifier"); }  //$NON-NLS-1$
		    consumeLabel() ;  
			break;
 
     case 422 : if (DEBUG) { System.out.println("ExpressionStatement ::= StatementExpression SEMICOLON"); }  //$NON-NLS-1$
		    consumeExpressionStatement();  
			break;
 
    case 431 : if (DEBUG) { System.out.println("IfThenStatement ::= if LPAREN Expression RPAREN..."); }  //$NON-NLS-1$
		    consumeStatementIfNoElse();  
			break;
 
    case 432 : if (DEBUG) { System.out.println("IfThenElseStatement ::= if LPAREN Expression RPAREN..."); }  //$NON-NLS-1$
		    consumeStatementIfWithElse();  
			break;
 
    case 433 : if (DEBUG) { System.out.println("IfThenElseStatementNoShortIf ::= if LPAREN Expression..."); }  //$NON-NLS-1$
		    consumeStatementIfWithElse();  
			break;
 
    case 434 : if (DEBUG) { System.out.println("SwitchStatement ::= switch LPAREN Expression RPAREN..."); }  //$NON-NLS-1$
		    consumeStatementSwitch() ;  
			break;
 
    case 435 : if (DEBUG) { System.out.println("SwitchBlock ::= LBRACE RBRACE"); }  //$NON-NLS-1$
		    consumeEmptySwitchBlock() ;  
			break;
 
    case 438 : if (DEBUG) { System.out.println("SwitchBlock ::= LBRACE SwitchBlockStatements..."); }  //$NON-NLS-1$
		    consumeSwitchBlock() ;  
			break;
 
    case 440 : if (DEBUG) { System.out.println("SwitchBlockStatements ::= SwitchBlockStatements..."); }  //$NON-NLS-1$
		    consumeSwitchBlockStatements() ;  
			break;
 
    case 441 : if (DEBUG) { System.out.println("SwitchBlockStatement ::= SwitchLabels BlockStatements"); }  //$NON-NLS-1$
		    consumeSwitchBlockStatement() ;  
			break;
 
    case 443 : if (DEBUG) { System.out.println("SwitchLabels ::= SwitchLabels SwitchLabel"); }  //$NON-NLS-1$
		    consumeSwitchLabels() ;  
			break;
 
     case 444 : if (DEBUG) { System.out.println("SwitchLabel ::= case ConstantExpression COLON"); }  //$NON-NLS-1$
		    consumeCaseLabel();  
			break;
 
     case 445 : if (DEBUG) { System.out.println("SwitchLabel ::= default COLON"); }  //$NON-NLS-1$
		    consumeDefaultLabel();  
			break;
 
    case 446 : if (DEBUG) { System.out.println("WhileStatement ::= while LPAREN Expression RPAREN..."); }  //$NON-NLS-1$
		    consumeStatementWhile() ;  
			break;
 
    case 447 : if (DEBUG) { System.out.println("WhileStatementNoShortIf ::= while LPAREN Expression..."); }  //$NON-NLS-1$
		    consumeStatementWhile() ;  
			break;
 
    case 448 : if (DEBUG) { System.out.println("DoStatement ::= do Statement while LPAREN Expression..."); }  //$NON-NLS-1$
		    consumeStatementDo() ;  
			break;
 
    case 449 : if (DEBUG) { System.out.println("ForStatement ::= for LPAREN ForInitopt SEMICOLON..."); }  //$NON-NLS-1$
		    consumeStatementFor() ;  
			break;
 
    case 450 : if (DEBUG) { System.out.println("ForStatementNoShortIf ::= for LPAREN ForInitopt..."); }  //$NON-NLS-1$
		    consumeStatementFor() ;  
			break;
 
    case 451 : if (DEBUG) { System.out.println("ForInit ::= StatementExpressionList"); }  //$NON-NLS-1$
		    consumeForInit() ;  
			break;
 
    case 455 : if (DEBUG) { System.out.println("StatementExpressionList ::= StatementExpressionList..."); }  //$NON-NLS-1$
		    consumeStatementExpressionList() ;  
			break;
 
    case 456 : if (DEBUG) { System.out.println("AssertStatement ::= assert Expression SEMICOLON"); }  //$NON-NLS-1$
		    consumeSimpleAssertStatement() ;  
			break;
 
    case 457 : if (DEBUG) { System.out.println("AssertStatement ::= assert Expression COLON Expression"); }  //$NON-NLS-1$
		    consumeAssertStatement() ;  
			break;
 
    case 458 : if (DEBUG) { System.out.println("BreakStatement ::= break SEMICOLON"); }  //$NON-NLS-1$
		    consumeStatementBreak() ;  
			break;
 
    case 463 : if (DEBUG) { System.out.println("BreakStatement ::= break Identifier SEMICOLON"); }  //$NON-NLS-1$
		    consumeStatementBreakWithLabel() ;  
			break;
 
    case 464 : if (DEBUG) { System.out.println("ContinueStatement ::= continue SEMICOLON"); }  //$NON-NLS-1$
		    consumeStatementContinue() ;  
			break;
 
    case 469 : if (DEBUG) { System.out.println("ContinueStatement ::= continue Identifier SEMICOLON"); }  //$NON-NLS-1$
		    consumeStatementContinueWithLabel() ;  
			break;
 
    case 470 : if (DEBUG) { System.out.println("ReturnStatement ::= return Expressionopt SEMICOLON"); }  //$NON-NLS-1$
		    consumeStatementReturn() ;  
			break;
 
    case 471 : if (DEBUG) { System.out.println("ThrowStatement ::= throw Expression SEMICOLON"); }  //$NON-NLS-1$
		    consumeStatementThrow();  
			break;
 
    case 472 : if (DEBUG) { System.out.println("SynchronizedStatement ::= OnlySynchronized LPAREN..."); }  //$NON-NLS-1$
		    consumeStatementSynchronized();  
			break;
 
    case 473 : if (DEBUG) { System.out.println("OnlySynchronized ::= synchronized"); }  //$NON-NLS-1$
		    consumeOnlySynchronized();  
			break;
 
    case 474 : if (DEBUG) { System.out.println("TryStatement ::= try TryBlock Catches"); }  //$NON-NLS-1$
		    consumeStatementTry(false, false);  
			break;
 
    case 475 : if (DEBUG) { System.out.println("TryStatement ::= try TryBlock Catchesopt Finally"); }  //$NON-NLS-1$
		    consumeStatementTry(true, false);  
			break;
 
    case 476 : if (DEBUG) { System.out.println("TryStatementWithResources ::= try ResourceSpecification"); }  //$NON-NLS-1$
		    consumeStatementTry(false, true);  
			break;
 
    case 477 : if (DEBUG) { System.out.println("TryStatementWithResources ::= try ResourceSpecification"); }  //$NON-NLS-1$
		    consumeStatementTry(true, true);  
			break;
 
    case 478 : if (DEBUG) { System.out.println("ResourceSpecification ::= LPAREN Resources ;opt RPAREN"); }  //$NON-NLS-1$
		    consumeResourceSpecification();  
			break;
 
    case 479 : if (DEBUG) { System.out.println(";opt ::="); }  //$NON-NLS-1$
		    consumeResourceOptionalTrailingSemiColon(false);  
			break;
 
    case 480 : if (DEBUG) { System.out.println(";opt ::= SEMICOLON"); }  //$NON-NLS-1$
		    consumeResourceOptionalTrailingSemiColon(true);  
			break;
 
    case 481 : if (DEBUG) { System.out.println("Resources ::= Resource"); }  //$NON-NLS-1$
		    consumeSingleResource();  
			break;
 
    case 482 : if (DEBUG) { System.out.println("Resources ::= Resources TrailingSemiColon Resource"); }  //$NON-NLS-1$
		    consumeMultipleResources();  
			break;
 
    case 483 : if (DEBUG) { System.out.println("TrailingSemiColon ::= SEMICOLON"); }  //$NON-NLS-1$
		    consumeResourceOptionalTrailingSemiColon(true);  
			break;
 
    case 484 : if (DEBUG) { System.out.println("Resource ::= Type PushModifiers VariableDeclaratorId..."); }  //$NON-NLS-1$
		    consumeResourceAsLocalVariableDeclaration();  
			break;
 
    case 485 : if (DEBUG) { System.out.println("Resource ::= Modifiers Type PushRealModifiers..."); }  //$NON-NLS-1$
		    consumeResourceAsLocalVariableDeclaration();  
			break;
 
    case 487 : if (DEBUG) { System.out.println("ExitTryBlock ::="); }  //$NON-NLS-1$
		    consumeExitTryBlock();  
			break;
 
    case 489 : if (DEBUG) { System.out.println("Catches ::= Catches CatchClause"); }  //$NON-NLS-1$
		    consumeCatches();  
			break;
 
    case 490 : if (DEBUG) { System.out.println("CatchClause ::= catch LPAREN CatchFormalParameter RPAREN"); }  //$NON-NLS-1$
		    consumeStatementCatch() ;  
			break;
 
    case 492 : if (DEBUG) { System.out.println("PushLPAREN ::= LPAREN"); }  //$NON-NLS-1$
		    consumeLeftParen();  
			break;
 
    case 493 : if (DEBUG) { System.out.println("PushRPAREN ::= RPAREN"); }  //$NON-NLS-1$
		    consumeRightParen();  
			break;
 
    case 498 : if (DEBUG) { System.out.println("PrimaryNoNewArray ::= this"); }  //$NON-NLS-1$
		    consumePrimaryNoNewArrayThis();  
			break;
 
    case 499 : if (DEBUG) { System.out.println("PrimaryNoNewArray ::= PushLPAREN Expression_NotName..."); }  //$NON-NLS-1$
		    consumePrimaryNoNewArray();  
			break;
 
    case 500 : if (DEBUG) { System.out.println("PrimaryNoNewArray ::= PushLPAREN Name PushRPAREN"); }  //$NON-NLS-1$
		    consumePrimaryNoNewArrayWithName();  
			break;
 
    case 503 : if (DEBUG) { System.out.println("PrimaryNoNewArray ::= Name DOT this"); }  //$NON-NLS-1$
		    consumePrimaryNoNewArrayNameThis();  
			break;
 
    case 504 : if (DEBUG) { System.out.println("PrimaryNoNewArray ::= Name DOT super"); }  //$NON-NLS-1$
		    consumePrimaryNoNewArrayNameSuper();  
			break;
 
    case 505 : if (DEBUG) { System.out.println("PrimaryNoNewArray ::= Name DOT class"); }  //$NON-NLS-1$
		    consumePrimaryNoNewArrayName();  
			break;
 
    case 506 : if (DEBUG) { System.out.println("PrimaryNoNewArray ::= Name Dims DOT class"); }  //$NON-NLS-1$
		    consumePrimaryNoNewArrayArrayType();  
			break;
 
    case 507 : if (DEBUG) { System.out.println("PrimaryNoNewArray ::= PrimitiveType Dims DOT class"); }  //$NON-NLS-1$
		    consumePrimaryNoNewArrayPrimitiveArrayType();  
			break;
 
    case 508 : if (DEBUG) { System.out.println("PrimaryNoNewArray ::= PrimitiveType DOT class"); }  //$NON-NLS-1$
		    consumePrimaryNoNewArrayPrimitiveType();  
			break;
 
    case 513 : if (DEBUG) { System.out.println("FunctionExpression ::= function Type PushLPAREN..."); }  //$NON-NLS-1$
		    consumeFunctionExpression();  
			break;
 
    case 516 : if (DEBUG) { System.out.println("ReferenceExpressionTypeArgumentsAndTrunk0 ::=..."); }  //$NON-NLS-1$
		    consumeReferenceExpressionTypeArgumentsAndTrunk(false);  
			break;
 
    case 517 : if (DEBUG) { System.out.println("ReferenceExpressionTypeArgumentsAndTrunk0 ::=..."); }  //$NON-NLS-1$
		    consumeReferenceExpressionTypeArgumentsAndTrunk(true);  
			break;
 
    case 518 : if (DEBUG) { System.out.println("ReferenceExpression ::= PrimitiveType Dims COLON_COLON"); }  //$NON-NLS-1$
		    consumeReferenceExpressionTypeForm(true);  
			break;
 
    case 519 : if (DEBUG) { System.out.println("ReferenceExpression ::= Name Dimsopt COLON_COLON..."); }  //$NON-NLS-1$
		    consumeReferenceExpressionTypeForm(false);  
			break;
 
    case 520 : if (DEBUG) { System.out.println("ReferenceExpression ::= Name BeginTypeArguments..."); }  //$NON-NLS-1$
		    consumeReferenceExpressionGenericTypeForm();  
			break;
 
    case 521 : if (DEBUG) { System.out.println("ReferenceExpression ::= Primary COLON_COLON..."); }  //$NON-NLS-1$
		    consumeReferenceExpressionPrimaryForm();  
			break;
 
    case 522 : if (DEBUG) { System.out.println("ReferenceExpression ::= super COLON_COLON..."); }  //$NON-NLS-1$
		    consumeReferenceExpressionSuperForm();  
			break;
 
    case 523 : if (DEBUG) { System.out.println("NonWildTypeArgumentsopt ::="); }  //$NON-NLS-1$
		    consumeEmptyTypeArguments();  
			break;
 
    case 525 : if (DEBUG) { System.out.println("IdentifierOrNew ::= Identifier"); }  //$NON-NLS-1$
		    consumeIdentifierOrNew(false);  
			break;
 
    case 526 : if (DEBUG) { System.out.println("IdentifierOrNew ::= new"); }  //$NON-NLS-1$
		    consumeIdentifierOrNew(true);  
			break;
 
    case 527 : if (DEBUG) { System.out.println("LambdaExpression ::= LambdaParameters ARROW LambdaBody"); }  //$NON-NLS-1$
		    consumeLambdaExpression();  
			break;
 
    case 528 : if (DEBUG) { System.out.println("NestedLambda ::="); }  //$NON-NLS-1$
		    consumeNestedLambda();  
			break;
 
    case 529 : if (DEBUG) { System.out.println("LambdaParameters ::= Identifier NestedLambda"); }  //$NON-NLS-1$
		    consumeTypeElidedLambdaParameter(false);  
			break;
 
    case 535 : if (DEBUG) { System.out.println("TypeElidedFormalParameterList ::=..."); }  //$NON-NLS-1$
		    consumeFormalParameterList();  
			break;
 
    case 536 : if (DEBUG) { System.out.println("TypeElidedFormalParameter ::= Modifiersopt Identifier"); }  //$NON-NLS-1$
		    consumeTypeElidedLambdaParameter(true);  
			break;
 
    case 539 : if (DEBUG) { System.out.println("ElidedLeftBraceAndReturn ::="); }  //$NON-NLS-1$
		    consumeElidedLeftBraceAndReturn();  
			break;
 
    case 540 : if (DEBUG) { System.out.println("AllocationHeader ::= new ClassType LPAREN..."); }  //$NON-NLS-1$
		    consumeAllocationHeader();  
			break;
 
    case 541 : if (DEBUG) { System.out.println("ClassInstanceCreationExpression ::= new..."); }  //$NON-NLS-1$
		    consumeClassInstanceCreationExpressionWithTypeArguments();  
			break;
 
    case 542 : if (DEBUG) { System.out.println("ClassInstanceCreationExpression ::= new ClassType..."); }  //$NON-NLS-1$
		    consumeClassInstanceCreationExpression();  
			break;
 
    case 543 : if (DEBUG) { System.out.println("ClassInstanceCreationExpression ::= Primary DOT new..."); }  //$NON-NLS-1$
		    consumeClassInstanceCreationExpressionQualifiedWithTypeArguments() ;  
			break;
 
    case 544 : if (DEBUG) { System.out.println("ClassInstanceCreationExpression ::= Primary DOT new..."); }  //$NON-NLS-1$
		    consumeClassInstanceCreationExpressionQualified() ;  
			break;
 
    case 545 : if (DEBUG) { System.out.println("ClassInstanceCreationExpression ::=..."); }  //$NON-NLS-1$
		    consumeClassInstanceCreationExpressionQualified() ;  
			break;
 
    case 546 : if (DEBUG) { System.out.println("ClassInstanceCreationExpression ::=..."); }  //$NON-NLS-1$
		    consumeClassInstanceCreationExpressionQualifiedWithTypeArguments() ;  
			break;
 
    case 547 : if (DEBUG) { System.out.println("EnterInstanceCreationArgumentList ::="); }  //$NON-NLS-1$
		    consumeEnterInstanceCreationArgumentList();  
			break;
 
    case 548 : if (DEBUG) { System.out.println("ClassInstanceCreationExpressionName ::= Name DOT"); }  //$NON-NLS-1$
		    consumeClassInstanceCreationExpressionName() ;  
			break;
 
    case 549 : if (DEBUG) { System.out.println("UnqualifiedClassBodyopt ::="); }  //$NON-NLS-1$
		    consumeClassBodyopt();  
			break;
 
    case 551 : if (DEBUG) { System.out.println("UnqualifiedEnterAnonymousClassBody ::="); }  //$NON-NLS-1$
		    consumeEnterAnonymousClassBody(false);  
			break;
 
    case 552 : if (DEBUG) { System.out.println("QualifiedClassBodyopt ::="); }  //$NON-NLS-1$
		    consumeClassBodyopt();  
			break;
 
    case 554 : if (DEBUG) { System.out.println("QualifiedEnterAnonymousClassBody ::="); }  //$NON-NLS-1$
		    consumeEnterAnonymousClassBody(true);  
			break;
 
    case 556 : if (DEBUG) { System.out.println("ArgumentList ::= ArgumentList COMMA Argument"); }  //$NON-NLS-1$
		    consumeArgumentList();  
			break;
 
    case 558 : if (DEBUG) { System.out.println("Argument ::= ref Expression"); }  //$NON-NLS-1$
		    consumeArgument(ASTNode.IsRefArgument);  
			break;
 
    case 559 : if (DEBUG) { System.out.println("Argument ::= out Expression"); }  //$NON-NLS-1$
		    consumeArgument(ASTNode.IsOutArgument);  
			break;
 
    case 560 : if (DEBUG) { System.out.println("ArrayCreationHeader ::= new PrimitiveType..."); }  //$NON-NLS-1$
		    consumeArrayCreationHeader();  
			break;
 
    case 561 : if (DEBUG) { System.out.println("ArrayCreationHeader ::= new ClassOrInterfaceType..."); }  //$NON-NLS-1$
		    consumeArrayCreationHeader();  
			break;
 
    case 562 : if (DEBUG) { System.out.println("ArrayCreationWithoutArrayInitializer ::= new..."); }  //$NON-NLS-1$
		    consumeArrayCreationExpressionWithoutInitializer();  
			break;
 
    case 563 : if (DEBUG) { System.out.println("ArrayCreationWithArrayInitializer ::= new PrimitiveType"); }  //$NON-NLS-1$
		    consumeArrayCreationExpressionWithInitializer();  
			break;
 
    case 564 : if (DEBUG) { System.out.println("ArrayCreationWithoutArrayInitializer ::= new..."); }  //$NON-NLS-1$
		    consumeArrayCreationExpressionWithoutInitializer();  
			break;
 
    case 565 : if (DEBUG) { System.out.println("ArrayCreationWithArrayInitializer ::= new..."); }  //$NON-NLS-1$
		    consumeArrayCreationExpressionWithInitializer();  
			break;
 
    case 567 : if (DEBUG) { System.out.println("DimWithOrWithOutExprs ::= DimWithOrWithOutExprs..."); }  //$NON-NLS-1$
		    consumeDimWithOrWithOutExprs();  
			break;
 
     case 569 : if (DEBUG) { System.out.println("DimWithOrWithOutExpr ::= LBRACKET RBRACKET"); }  //$NON-NLS-1$
		    consumeDimWithOrWithOutExpr();  
			break;
 
     case 570 : if (DEBUG) { System.out.println("Dims ::= DimsLoop"); }  //$NON-NLS-1$
		    consumeDims();  
			break;
 
     case 573 : if (DEBUG) { System.out.println("OneDimLoop ::= LBRACKET RBRACKET"); }  //$NON-NLS-1$
		    consumeOneDimLoop(false);  
			break;
 
    case 578 : if (DEBUG) { System.out.println("FieldAccess ::= Primary DOT Identifier"); }  //$NON-NLS-1$
		    consumeFieldAccess(false);  
			break;
 
    case 583 : if (DEBUG) { System.out.println("FieldAccess ::= super DOT Identifier"); }  //$NON-NLS-1$
		    consumeFieldAccess(true);  
			break;
 
    case 584 : if (DEBUG) { System.out.println("MethodInvocation ::= Name LPAREN ArgumentListopt RPAREN"); }  //$NON-NLS-1$
		    consumeMethodInvocationName();  
			break;
 
    case 589 : if (DEBUG) { System.out.println("MethodInvocation ::= Name DOT OnlyTypeArguments..."); }  //$NON-NLS-1$
		    consumeMethodInvocationNameWithTypeArguments();  
			break;
 
    case 590 : if (DEBUG) { System.out.println("MethodInvocation ::= MethodInvocation LPAREN..."); }  //$NON-NLS-1$
		    consumeMethodInvocationName();  
			break;
 
    case 597 : if (DEBUG) { System.out.println("MethodInvocation ::= Primary DOT OnlyTypeArguments..."); }  //$NON-NLS-1$
		    consumeMethodInvocationPrimaryWithTypeArguments();  
			break;
 
    case 602 : if (DEBUG) { System.out.println("MethodInvocation ::= Primary DOT Identifier LPAREN..."); }  //$NON-NLS-1$
		    consumeMethodInvocationPrimary();  
			break;
 
    case 607 : if (DEBUG) { System.out.println("MethodInvocation ::= super DOT OnlyTypeArguments..."); }  //$NON-NLS-1$
		    consumeMethodInvocationSuperWithTypeArguments();  
			break;
 
    case 612 : if (DEBUG) { System.out.println("MethodInvocation ::= super DOT Identifier LPAREN..."); }  //$NON-NLS-1$
		    consumeMethodInvocationSuper();  
			break;
 
    case 613 : if (DEBUG) { System.out.println("ArrayAccess ::= Name LBRACKET Expression RBRACKET"); }  //$NON-NLS-1$
		    consumeArrayAccess(true);  
			break;
 
    case 614 : if (DEBUG) { System.out.println("ArrayAccess ::= PrimaryNoNewArray LBRACKET Expression..."); }  //$NON-NLS-1$
		    consumeArrayAccess(false);  
			break;
 
    case 615 : if (DEBUG) { System.out.println("ArrayAccess ::= ArrayCreationWithArrayInitializer..."); }  //$NON-NLS-1$
		    consumeArrayAccess(false);  
			break;
 
    case 617 : if (DEBUG) { System.out.println("PostfixExpression ::= Name"); }  //$NON-NLS-1$
		    consumePostfixExpression();  
			break;
 
    case 620 : if (DEBUG) { System.out.println("PostIncrementExpression ::= PostfixExpression PLUS_PLUS"); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.PLUS,true);  
			break;
 
    case 621 : if (DEBUG) { System.out.println("PostDecrementExpression ::= PostfixExpression..."); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.MINUS,true);  
			break;
 
    case 622 : if (DEBUG) { System.out.println("PushPosition ::="); }  //$NON-NLS-1$
		    consumePushPosition();  
			break;
 
    case 625 : if (DEBUG) { System.out.println("UnaryExpression ::= PLUS PushPosition UnaryExpression"); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.PLUS);  
			break;
 
    case 626 : if (DEBUG) { System.out.println("UnaryExpression ::= MINUS PushPosition UnaryExpression"); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.MINUS);  
			break;
 
    case 628 : if (DEBUG) { System.out.println("PreIncrementExpression ::= PLUS_PLUS PushPosition..."); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.PLUS,false);  
			break;
 
    case 629 : if (DEBUG) { System.out.println("PreDecrementExpression ::= MINUS_MINUS PushPosition..."); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.MINUS,false);  
			break;
 
    case 631 : if (DEBUG) { System.out.println("UnaryExpressionNotPlusMinus ::= TWIDDLE PushPosition..."); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.TWIDDLE);  
			break;
 
    case 632 : if (DEBUG) { System.out.println("UnaryExpressionNotPlusMinus ::= NOT PushPosition..."); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.NOT);  
			break;
 
    case 634 : if (DEBUG) { System.out.println("CastExpression ::= PushLPAREN PrimitiveType Dimsopt..."); }  //$NON-NLS-1$
		    consumeCastExpressionWithPrimitiveType();  
			break;
 
    case 635 : if (DEBUG) { System.out.println("CastExpression ::= PushLPAREN Name..."); }  //$NON-NLS-1$
		    consumeCastExpressionWithGenericsArray();  
			break;
 
    case 636 : if (DEBUG) { System.out.println("CastExpression ::= PushLPAREN Name..."); }  //$NON-NLS-1$
		    consumeCastExpressionWithQualifiedGenericsArray();  
			break;
 
    case 637 : if (DEBUG) { System.out.println("CastExpression ::= PushLPAREN Name PushRPAREN..."); }  //$NON-NLS-1$
		    consumeCastExpressionLL1();  
			break;
 
    case 638 : if (DEBUG) { System.out.println("CastExpression ::= BeginIntersectionCast PushLPAREN..."); }  //$NON-NLS-1$
		    consumeCastExpressionLL1WithBounds();  
			break;
 
    case 639 : if (DEBUG) { System.out.println("CastExpression ::= PushLPAREN Name Dims..."); }  //$NON-NLS-1$
		    consumeCastExpressionWithNameArray();  
			break;
 
    case 640 : if (DEBUG) { System.out.println("AdditionalBoundsListOpt ::="); }  //$NON-NLS-1$
		    consumeZeroAdditionalBounds();  
			break;
 
    case 644 : if (DEBUG) { System.out.println("OnlyTypeArgumentsForCastExpression ::= OnlyTypeArguments"); }  //$NON-NLS-1$
		    consumeOnlyTypeArgumentsForCastExpression();  
			break;
 
    case 645 : if (DEBUG) { System.out.println("InsideCastExpression ::="); }  //$NON-NLS-1$
		    consumeInsideCastExpression();  
			break;
 
    case 646 : if (DEBUG) { System.out.println("InsideCastExpressionLL1 ::="); }  //$NON-NLS-1$
		    consumeInsideCastExpressionLL1();  
			break;
 
    case 647 : if (DEBUG) { System.out.println("InsideCastExpressionLL1WithBounds ::="); }  //$NON-NLS-1$
		    consumeInsideCastExpressionLL1WithBounds ();  
			break;
 
    case 648 : if (DEBUG) { System.out.println("InsideCastExpressionWithQualifiedGenerics ::="); }  //$NON-NLS-1$
		    consumeInsideCastExpressionWithQualifiedGenerics();  
			break;
 
    case 650 : if (DEBUG) { System.out.println("MultiplicativeExpression ::= MultiplicativeExpression..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.MULTIPLY);  
			break;
 
    case 651 : if (DEBUG) { System.out.println("MultiplicativeExpression ::= MultiplicativeExpression..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.DIVIDE);  
			break;
 
    case 652 : if (DEBUG) { System.out.println("MultiplicativeExpression ::= MultiplicativeExpression..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.REMAINDER);  
			break;
 
    case 654 : if (DEBUG) { System.out.println("AdditiveExpression ::= AdditiveExpression PLUS..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.PLUS);  
			break;
 
    case 655 : if (DEBUG) { System.out.println("AdditiveExpression ::= AdditiveExpression MINUS..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.MINUS);  
			break;
 
    case 657 : if (DEBUG) { System.out.println("ShiftExpression ::= ShiftExpression LEFT_SHIFT..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.LEFT_SHIFT);  
			break;
 
    case 658 : if (DEBUG) { System.out.println("ShiftExpression ::= ShiftExpression RIGHT_SHIFT..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.RIGHT_SHIFT);  
			break;
 
    case 659 : if (DEBUG) { System.out.println("ShiftExpression ::= ShiftExpression UNSIGNED_RIGHT_SHIFT"); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.UNSIGNED_RIGHT_SHIFT);  
			break;
 
    case 661 : if (DEBUG) { System.out.println("RelationalExpression ::= RelationalExpression LESS..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.LESS);  
			break;
 
    case 662 : if (DEBUG) { System.out.println("RelationalExpression ::= RelationalExpression GREATER..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.GREATER);  
			break;
 
    case 663 : if (DEBUG) { System.out.println("RelationalExpression ::= RelationalExpression LESS_EQUAL"); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.LESS_EQUAL);  
			break;
 
    case 664 : if (DEBUG) { System.out.println("RelationalExpression ::= RelationalExpression..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.GREATER_EQUAL);  
			break;
 
    case 666 : if (DEBUG) { System.out.println("InstanceofExpression ::= InstanceofExpression instanceof"); }  //$NON-NLS-1$
		    consumeInstanceOfExpression();  
			break;
 
    case 668 : if (DEBUG) { System.out.println("EqualityExpression ::= EqualityExpression EQUAL_EQUAL..."); }  //$NON-NLS-1$
		    consumeEqualityExpression(OperatorIds.EQUAL_EQUAL);  
			break;
 
    case 669 : if (DEBUG) { System.out.println("EqualityExpression ::= EqualityExpression NOT_EQUAL..."); }  //$NON-NLS-1$
		    consumeEqualityExpression(OperatorIds.NOT_EQUAL);  
			break;
 
    case 671 : if (DEBUG) { System.out.println("AndExpression ::= AndExpression AND EqualityExpression"); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.AND);  
			break;
 
    case 673 : if (DEBUG) { System.out.println("ExclusiveOrExpression ::= ExclusiveOrExpression XOR..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.XOR);  
			break;
 
    case 675 : if (DEBUG) { System.out.println("InclusiveOrExpression ::= InclusiveOrExpression OR..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.OR);  
			break;
 
    case 677 : if (DEBUG) { System.out.println("ConditionalAndExpression ::= ConditionalAndExpression..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.AND_AND);  
			break;
 
    case 679 : if (DEBUG) { System.out.println("ConditionalOrExpression ::= ConditionalOrExpression..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.OR_OR);  
			break;
 
    case 681 : if (DEBUG) { System.out.println("ConditionalExpression ::= ConditionalOrExpression..."); }  //$NON-NLS-1$
		    consumeConditionalExpression(OperatorIds.QUESTIONCOLON) ;  
			break;
 
    case 684 : if (DEBUG) { System.out.println("Assignment ::= PostfixExpression AssignmentOperator..."); }  //$NON-NLS-1$
		    consumeAssignment();  
			break;
 
    case 686 : if (DEBUG) { System.out.println("Assignment ::= InvalidArrayInitializerAssignement"); }  //$NON-NLS-1$
		    ignoreExpressionAssignment(); 
			break;
 
    case 687 : if (DEBUG) { System.out.println("AssignmentOperator ::= EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(EQUAL);  
			break;
 
    case 688 : if (DEBUG) { System.out.println("AssignmentOperator ::= MULTIPLY_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(MULTIPLY);  
			break;
 
    case 689 : if (DEBUG) { System.out.println("AssignmentOperator ::= DIVIDE_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(DIVIDE);  
			break;
 
    case 690 : if (DEBUG) { System.out.println("AssignmentOperator ::= REMAINDER_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(REMAINDER);  
			break;
 
    case 691 : if (DEBUG) { System.out.println("AssignmentOperator ::= PLUS_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(PLUS);  
			break;
 
    case 692 : if (DEBUG) { System.out.println("AssignmentOperator ::= MINUS_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(MINUS);  
			break;
 
    case 693 : if (DEBUG) { System.out.println("AssignmentOperator ::= LEFT_SHIFT_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(LEFT_SHIFT);  
			break;
 
    case 694 : if (DEBUG) { System.out.println("AssignmentOperator ::= RIGHT_SHIFT_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(RIGHT_SHIFT);  
			break;
 
    case 695 : if (DEBUG) { System.out.println("AssignmentOperator ::= UNSIGNED_RIGHT_SHIFT_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(UNSIGNED_RIGHT_SHIFT);  
			break;
 
    case 696 : if (DEBUG) { System.out.println("AssignmentOperator ::= AND_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(AND);  
			break;
 
    case 697 : if (DEBUG) { System.out.println("AssignmentOperator ::= XOR_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(XOR);  
			break;
 
    case 698 : if (DEBUG) { System.out.println("AssignmentOperator ::= OR_EQUAL"); }  //$NON-NLS-1$
		    consumeAssignmentOperator(OR);  
			break;
 
    case 699 : if (DEBUG) { System.out.println("Expression ::= AssignmentExpression"); }  //$NON-NLS-1$
		    consumeExpression();  
			break;
 
    case 702 : if (DEBUG) { System.out.println("Expressionopt ::="); }  //$NON-NLS-1$
		    consumeEmptyExpression();  
			break;
 
    case 707 : if (DEBUG) { System.out.println("ClassBodyDeclarationsopt ::="); }  //$NON-NLS-1$
		    consumeEmptyClassBodyDeclarationsopt();  
			break;
 
    case 708 : if (DEBUG) { System.out.println("ClassBodyDeclarationsopt ::= NestedType..."); }  //$NON-NLS-1$
		    consumeClassBodyDeclarationsopt();  
			break;
 
     case 709 : if (DEBUG) { System.out.println("Modifiersopt ::="); }  //$NON-NLS-1$
		    consumeDefaultModifiers();  
			break;
 
    case 710 : if (DEBUG) { System.out.println("Modifiersopt ::= Modifiers"); }  //$NON-NLS-1$
		    consumeModifiers();  
			break;
 
    case 711 : if (DEBUG) { System.out.println("BlockStatementsopt ::="); }  //$NON-NLS-1$
		    consumeEmptyBlockStatementsopt();  
			break;
 
     case 713 : if (DEBUG) { System.out.println("Dimsopt ::="); }  //$NON-NLS-1$
		    consumeEmptyDimsopt();  
			break;
 
     case 715 : if (DEBUG) { System.out.println("ArgumentListopt ::="); }  //$NON-NLS-1$
		    consumeEmptyArgumentListopt();  
			break;
 
    case 719 : if (DEBUG) { System.out.println("FormalParameterListopt ::="); }  //$NON-NLS-1$
		    consumeFormalParameterListopt();  
			break;
 
     case 723 : if (DEBUG) { System.out.println("InterfaceMemberDeclarationsopt ::="); }  //$NON-NLS-1$
		    consumeEmptyInterfaceMemberDeclarationsopt();  
			break;
 
     case 724 : if (DEBUG) { System.out.println("InterfaceMemberDeclarationsopt ::= NestedType..."); }  //$NON-NLS-1$
		    consumeInterfaceMemberDeclarationsopt();  
			break;
 
    case 725 : if (DEBUG) { System.out.println("NestedType ::="); }  //$NON-NLS-1$
		    consumeNestedType();  
			break;

     case 726 : if (DEBUG) { System.out.println("ForInitopt ::="); }  //$NON-NLS-1$
		    consumeEmptyForInitopt();  
			break;
 
     case 728 : if (DEBUG) { System.out.println("ForUpdateopt ::="); }  //$NON-NLS-1$
		    consumeEmptyForUpdateopt();  
			break;
 
     case 732 : if (DEBUG) { System.out.println("Catchesopt ::="); }  //$NON-NLS-1$
		    consumeEmptyCatchesopt();  
			break;
 
     case 734 : if (DEBUG) { System.out.println("EnumDeclaration ::= EnumHeader EnumBody"); }  //$NON-NLS-1$
		    consumeEnumDeclaration();  
			break;
 
     case 735 : if (DEBUG) { System.out.println("EnumHeader ::= EnumHeaderName ClassHeaderImplementsopt"); }  //$NON-NLS-1$
		    consumeEnumHeader();  
			break;
 
     case 736 : if (DEBUG) { System.out.println("EnumHeaderName ::= Modifiersopt enum Identifier"); }  //$NON-NLS-1$
		    consumeEnumHeaderName();  
			break;
 
     case 737 : if (DEBUG) { System.out.println("EnumHeaderName ::= Modifiersopt enum Identifier..."); }  //$NON-NLS-1$
		    consumeEnumHeaderNameWithTypeParameters();  
			break;
 
     case 738 : if (DEBUG) { System.out.println("EnumBody ::= LBRACE EnumBodyDeclarationsopt RBRACE"); }  //$NON-NLS-1$
		    consumeEnumBodyNoConstants();  
			break;
 
     case 739 : if (DEBUG) { System.out.println("EnumBody ::= LBRACE COMMA EnumBodyDeclarationsopt..."); }  //$NON-NLS-1$
		    consumeEnumBodyNoConstants();  
			break;
 
     case 740 : if (DEBUG) { System.out.println("EnumBody ::= LBRACE EnumConstants COMMA..."); }  //$NON-NLS-1$
		    consumeEnumBodyWithConstants();  
			break;
 
     case 741 : if (DEBUG) { System.out.println("EnumBody ::= LBRACE EnumConstants..."); }  //$NON-NLS-1$
		    consumeEnumBodyWithConstants();  
			break;
 
    case 743 : if (DEBUG) { System.out.println("EnumConstants ::= EnumConstants COMMA EnumConstant"); }  //$NON-NLS-1$
		    consumeEnumConstants();  
			break;
 
    case 744 : if (DEBUG) { System.out.println("EnumConstantHeaderName ::= Modifiersopt Identifier"); }  //$NON-NLS-1$
		    consumeEnumConstantHeaderName();  
			break;
 
    case 745 : if (DEBUG) { System.out.println("EnumConstantHeader ::= EnumConstantHeaderName..."); }  //$NON-NLS-1$
		    consumeEnumConstantHeader();  
			break;
 
    case 746 : if (DEBUG) { System.out.println("EnumConstant ::= EnumConstantHeader ForceNoDiet..."); }  //$NON-NLS-1$
		    consumeEnumConstantWithClassBody();  
			break;
 
    case 747 : if (DEBUG) { System.out.println("EnumConstant ::= EnumConstantHeader"); }  //$NON-NLS-1$
		    consumeEnumConstantNoClassBody();  
			break;
 
    case 748 : if (DEBUG) { System.out.println("Arguments ::= LPAREN ArgumentListopt RPAREN"); }  //$NON-NLS-1$
		    consumeArguments();  
			break;
 
    case 749 : if (DEBUG) { System.out.println("Argumentsopt ::="); }  //$NON-NLS-1$
		    consumeEmptyArguments();  
			break;
 
    case 751 : if (DEBUG) { System.out.println("EnumDeclarations ::= SEMICOLON ClassBodyDeclarationsopt"); }  //$NON-NLS-1$
		    consumeEnumDeclarations();  
			break;
 
    case 752 : if (DEBUG) { System.out.println("EnumBodyDeclarationsopt ::="); }  //$NON-NLS-1$
		    consumeEmptyEnumDeclarations();  
			break;
 
    case 754 : if (DEBUG) { System.out.println("EnhancedForStatement ::= EnhancedForStatementHeader..."); }  //$NON-NLS-1$
		    consumeEnhancedForStatement();  
			break;
 
    case 755 : if (DEBUG) { System.out.println("EnhancedForStatementNoShortIf ::=..."); }  //$NON-NLS-1$
		    consumeEnhancedForStatement();  
			break;
 
    case 756 : if (DEBUG) { System.out.println("EnhancedForStatementHeaderInit ::= for LPAREN Type..."); }  //$NON-NLS-1$
		    consumeEnhancedForStatementHeaderInit(false);  
			break;
 
    case 757 : if (DEBUG) { System.out.println("EnhancedForStatementHeaderInit ::= for LPAREN Modifiers"); }  //$NON-NLS-1$
		    consumeEnhancedForStatementHeaderInit(true);  
			break;
 
    case 758 : if (DEBUG) { System.out.println("EnhancedForStatementHeader ::=..."); }  //$NON-NLS-1$
		    consumeEnhancedForStatementHeader();  
			break;
 
    case 759 : if (DEBUG) { System.out.println("SingleStaticImportDeclaration ::=..."); }  //$NON-NLS-1$
		    consumeImportDeclaration();  
			break;
 
    case 760 : if (DEBUG) { System.out.println("SingleStaticImportDeclarationName ::= import static Name"); }  //$NON-NLS-1$
		    consumeSingleStaticImportDeclarationName();  
			break;
 
    case 761 : if (DEBUG) { System.out.println("StaticImportOnDemandDeclaration ::=..."); }  //$NON-NLS-1$
		    consumeImportDeclaration();  
			break;
 
    case 762 : if (DEBUG) { System.out.println("StaticImportOnDemandDeclarationName ::= import static..."); }  //$NON-NLS-1$
		    consumeStaticImportOnDemandDeclarationName();  
			break;
 
    case 763 : if (DEBUG) { System.out.println("TypeArguments ::= LESS TypeArgumentList1"); }  //$NON-NLS-1$
		    consumeTypeArguments();  
			break;
 
    case 764 : if (DEBUG) { System.out.println("OnlyTypeArguments ::= LESS TypeArgumentList1"); }  //$NON-NLS-1$
		    consumeOnlyTypeArguments();  
			break;
 
    case 766 : if (DEBUG) { System.out.println("TypeArgumentList1 ::= TypeArgumentList COMMA..."); }  //$NON-NLS-1$
		    consumeTypeArgumentList1();  
			break;
 
    case 768 : if (DEBUG) { System.out.println("TypeArgumentList ::= TypeArgumentList COMMA TypeArgument"); }  //$NON-NLS-1$
		    consumeTypeArgumentList();  
			break;
 
    case 769 : if (DEBUG) { System.out.println("TypeArgument ::= ReferenceType"); }  //$NON-NLS-1$
		    consumeTypeArgument();  
			break;
 
    case 773 : if (DEBUG) { System.out.println("ReferenceType1 ::= ReferenceType GREATER"); }  //$NON-NLS-1$
		    consumeReferenceType1();  
			break;
 
    case 774 : if (DEBUG) { System.out.println("ReferenceType1 ::= ClassOrInterface LESS..."); }  //$NON-NLS-1$
		    consumeTypeArgumentReferenceType1();  
			break;
 
    case 776 : if (DEBUG) { System.out.println("TypeArgumentList2 ::= TypeArgumentList COMMA..."); }  //$NON-NLS-1$
		    consumeTypeArgumentList2();  
			break;
 
    case 779 : if (DEBUG) { System.out.println("ReferenceType2 ::= ReferenceType RIGHT_SHIFT"); }  //$NON-NLS-1$
		    consumeReferenceType2();  
			break;
 
    case 780 : if (DEBUG) { System.out.println("ReferenceType2 ::= ClassOrInterface LESS..."); }  //$NON-NLS-1$
		    consumeTypeArgumentReferenceType2();  
			break;
 
    case 782 : if (DEBUG) { System.out.println("TypeArgumentList3 ::= TypeArgumentList COMMA..."); }  //$NON-NLS-1$
		    consumeTypeArgumentList3();  
			break;
 
    case 785 : if (DEBUG) { System.out.println("ReferenceType3 ::= ReferenceType UNSIGNED_RIGHT_SHIFT"); }  //$NON-NLS-1$
		    consumeReferenceType3();  
			break;
 
    case 786 : if (DEBUG) { System.out.println("Wildcard ::= QUESTION"); }  //$NON-NLS-1$
		    consumeWildcard();  
			break;
 
    case 787 : if (DEBUG) { System.out.println("Wildcard ::= QUESTION WildcardBounds"); }  //$NON-NLS-1$
		    consumeWildcardWithBounds();  
			break;
 
    case 788 : if (DEBUG) { System.out.println("WildcardBounds ::= extends ReferenceType"); }  //$NON-NLS-1$
		    consumeWildcardBoundsExtends();  
			break;
 
    case 789 : if (DEBUG) { System.out.println("WildcardBounds ::= super ReferenceType"); }  //$NON-NLS-1$
		    consumeWildcardBoundsSuper();  
			break;
 
    case 790 : if (DEBUG) { System.out.println("Wildcard1 ::= QUESTION GREATER"); }  //$NON-NLS-1$
		    consumeWildcard1();  
			break;
 
    case 791 : if (DEBUG) { System.out.println("Wildcard1 ::= QUESTION WildcardBounds1"); }  //$NON-NLS-1$
		    consumeWildcard1WithBounds();  
			break;
 
    case 792 : if (DEBUG) { System.out.println("WildcardBounds1 ::= extends ReferenceType1"); }  //$NON-NLS-1$
		    consumeWildcardBounds1Extends();  
			break;
 
    case 793 : if (DEBUG) { System.out.println("WildcardBounds1 ::= super ReferenceType1"); }  //$NON-NLS-1$
		    consumeWildcardBounds1Super();  
			break;
 
    case 794 : if (DEBUG) { System.out.println("Wildcard2 ::= QUESTION RIGHT_SHIFT"); }  //$NON-NLS-1$
		    consumeWildcard2();  
			break;
 
    case 795 : if (DEBUG) { System.out.println("Wildcard2 ::= QUESTION WildcardBounds2"); }  //$NON-NLS-1$
		    consumeWildcard2WithBounds();  
			break;
 
    case 796 : if (DEBUG) { System.out.println("WildcardBounds2 ::= extends ReferenceType2"); }  //$NON-NLS-1$
		    consumeWildcardBounds2Extends();  
			break;
 
    case 797 : if (DEBUG) { System.out.println("WildcardBounds2 ::= super ReferenceType2"); }  //$NON-NLS-1$
		    consumeWildcardBounds2Super();  
			break;
 
    case 798 : if (DEBUG) { System.out.println("Wildcard3 ::= QUESTION UNSIGNED_RIGHT_SHIFT"); }  //$NON-NLS-1$
		    consumeWildcard3();  
			break;
 
    case 799 : if (DEBUG) { System.out.println("Wildcard3 ::= QUESTION WildcardBounds3"); }  //$NON-NLS-1$
		    consumeWildcard3WithBounds();  
			break;
 
    case 800 : if (DEBUG) { System.out.println("WildcardBounds3 ::= extends ReferenceType3"); }  //$NON-NLS-1$
		    consumeWildcardBounds3Extends();  
			break;
 
    case 801 : if (DEBUG) { System.out.println("WildcardBounds3 ::= super ReferenceType3"); }  //$NON-NLS-1$
		    consumeWildcardBounds3Super();  
			break;
 
    case 802 : if (DEBUG) { System.out.println("TypeParameterHeader ::= Identifier"); }  //$NON-NLS-1$
		    consumeTypeParameterHeader();  
			break;
 
    case 803 : if (DEBUG) { System.out.println("TypeParameters ::= LESS TypeParameterList1"); }  //$NON-NLS-1$
		    consumeTypeParameters();  
			break;
 
    case 805 : if (DEBUG) { System.out.println("TypeParameterList ::= TypeParameterList COMMA..."); }  //$NON-NLS-1$
		    consumeTypeParameterList();  
			break;
 
    case 807 : if (DEBUG) { System.out.println("TypeParameter ::= TypeParameterHeader extends..."); }  //$NON-NLS-1$
		    consumeTypeParameterWithExtends();  
			break;
 
    case 808 : if (DEBUG) { System.out.println("TypeParameter ::= TypeParameterHeader extends..."); }  //$NON-NLS-1$
		    consumeTypeParameterWithExtendsAndBounds();  
			break;
 
    case 810 : if (DEBUG) { System.out.println("AdditionalBoundList ::= AdditionalBoundList..."); }  //$NON-NLS-1$
		    consumeAdditionalBoundList();  
			break;
 
    case 811 : if (DEBUG) { System.out.println("AdditionalBound ::= AND ReferenceType"); }  //$NON-NLS-1$
		    consumeAdditionalBound();  
			break;
 
    case 813 : if (DEBUG) { System.out.println("TypeParameterList1 ::= TypeParameterList COMMA..."); }  //$NON-NLS-1$
		    consumeTypeParameterList1();  
			break;
 
    case 814 : if (DEBUG) { System.out.println("TypeParameter1 ::= TypeParameterHeader GREATER"); }  //$NON-NLS-1$
		    consumeTypeParameter1();  
			break;
 
    case 815 : if (DEBUG) { System.out.println("TypeParameter1 ::= TypeParameterHeader extends..."); }  //$NON-NLS-1$
		    consumeTypeParameter1WithExtends();  
			break;
 
    case 816 : if (DEBUG) { System.out.println("TypeParameter1 ::= TypeParameterHeader extends..."); }  //$NON-NLS-1$
		    consumeTypeParameter1WithExtendsAndBounds();  
			break;
 
    case 818 : if (DEBUG) { System.out.println("AdditionalBoundList1 ::= AdditionalBoundList..."); }  //$NON-NLS-1$
		    consumeAdditionalBoundList1();  
			break;
 
    case 819 : if (DEBUG) { System.out.println("AdditionalBound1 ::= AND ReferenceType1"); }  //$NON-NLS-1$
		    consumeAdditionalBound1();  
			break;
 
    case 825 : if (DEBUG) { System.out.println("UnaryExpression_NotName ::= PLUS PushPosition..."); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.PLUS);  
			break;
 
    case 826 : if (DEBUG) { System.out.println("UnaryExpression_NotName ::= MINUS PushPosition..."); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.MINUS);  
			break;
 
    case 829 : if (DEBUG) { System.out.println("UnaryExpressionNotPlusMinus_NotName ::= TWIDDLE..."); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.TWIDDLE);  
			break;
 
    case 830 : if (DEBUG) { System.out.println("UnaryExpressionNotPlusMinus_NotName ::= NOT PushPosition"); }  //$NON-NLS-1$
		    consumeUnaryExpression(OperatorIds.NOT);  
			break;
 
    case 833 : if (DEBUG) { System.out.println("MultiplicativeExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.MULTIPLY);  
			break;
 
    case 834 : if (DEBUG) { System.out.println("MultiplicativeExpression_NotName ::= Name MULTIPLY..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.MULTIPLY);  
			break;
 
    case 835 : if (DEBUG) { System.out.println("MultiplicativeExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.DIVIDE);  
			break;
 
    case 836 : if (DEBUG) { System.out.println("MultiplicativeExpression_NotName ::= Name DIVIDE..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.DIVIDE);  
			break;
 
    case 837 : if (DEBUG) { System.out.println("MultiplicativeExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.REMAINDER);  
			break;
 
    case 838 : if (DEBUG) { System.out.println("MultiplicativeExpression_NotName ::= Name REMAINDER..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.REMAINDER);  
			break;
 
    case 840 : if (DEBUG) { System.out.println("AdditiveExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.PLUS);  
			break;
 
    case 841 : if (DEBUG) { System.out.println("AdditiveExpression_NotName ::= Name PLUS..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.PLUS);  
			break;
 
    case 842 : if (DEBUG) { System.out.println("AdditiveExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.MINUS);  
			break;
 
    case 843 : if (DEBUG) { System.out.println("AdditiveExpression_NotName ::= Name MINUS..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.MINUS);  
			break;
 
    case 845 : if (DEBUG) { System.out.println("ShiftExpression_NotName ::= ShiftExpression_NotName..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.LEFT_SHIFT);  
			break;
 
    case 846 : if (DEBUG) { System.out.println("ShiftExpression_NotName ::= Name LEFT_SHIFT..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.LEFT_SHIFT);  
			break;
 
    case 847 : if (DEBUG) { System.out.println("ShiftExpression_NotName ::= ShiftExpression_NotName..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.RIGHT_SHIFT);  
			break;
 
    case 848 : if (DEBUG) { System.out.println("ShiftExpression_NotName ::= Name RIGHT_SHIFT..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.RIGHT_SHIFT);  
			break;
 
    case 849 : if (DEBUG) { System.out.println("ShiftExpression_NotName ::= ShiftExpression_NotName..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.UNSIGNED_RIGHT_SHIFT);  
			break;
 
    case 850 : if (DEBUG) { System.out.println("ShiftExpression_NotName ::= Name UNSIGNED_RIGHT_SHIFT..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.UNSIGNED_RIGHT_SHIFT);  
			break;
 
    case 852 : if (DEBUG) { System.out.println("RelationalExpression_NotName ::= ShiftExpression_NotName"); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.LESS);  
			break;
 
    case 853 : if (DEBUG) { System.out.println("RelationalExpression_NotName ::= Name LESS..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.LESS);  
			break;
 
    case 854 : if (DEBUG) { System.out.println("RelationalExpression_NotName ::= ShiftExpression_NotName"); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.GREATER);  
			break;
 
    case 855 : if (DEBUG) { System.out.println("RelationalExpression_NotName ::= Name GREATER..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.GREATER);  
			break;
 
    case 856 : if (DEBUG) { System.out.println("RelationalExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.LESS_EQUAL);  
			break;
 
    case 857 : if (DEBUG) { System.out.println("RelationalExpression_NotName ::= Name LESS_EQUAL..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.LESS_EQUAL);  
			break;
 
    case 858 : if (DEBUG) { System.out.println("RelationalExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.GREATER_EQUAL);  
			break;
 
    case 859 : if (DEBUG) { System.out.println("RelationalExpression_NotName ::= Name GREATER_EQUAL..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.GREATER_EQUAL);  
			break;
 
    case 861 : if (DEBUG) { System.out.println("InstanceofExpression_NotName ::= Name instanceof..."); }  //$NON-NLS-1$
		    consumeInstanceOfExpressionWithName();  
			break;
 
    case 862 : if (DEBUG) { System.out.println("InstanceofExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeInstanceOfExpression();  
			break;
 
    case 864 : if (DEBUG) { System.out.println("EqualityExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeEqualityExpression(OperatorIds.EQUAL_EQUAL);  
			break;
 
    case 865 : if (DEBUG) { System.out.println("EqualityExpression_NotName ::= Name EQUAL_EQUAL..."); }  //$NON-NLS-1$
		    consumeEqualityExpressionWithName(OperatorIds.EQUAL_EQUAL);  
			break;
 
    case 866 : if (DEBUG) { System.out.println("EqualityExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeEqualityExpression(OperatorIds.NOT_EQUAL);  
			break;
 
    case 867 : if (DEBUG) { System.out.println("EqualityExpression_NotName ::= Name NOT_EQUAL..."); }  //$NON-NLS-1$
		    consumeEqualityExpressionWithName(OperatorIds.NOT_EQUAL);  
			break;
 
    case 869 : if (DEBUG) { System.out.println("AndExpression_NotName ::= AndExpression_NotName AND..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.AND);  
			break;
 
    case 870 : if (DEBUG) { System.out.println("AndExpression_NotName ::= Name AND EqualityExpression"); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.AND);  
			break;
 
    case 872 : if (DEBUG) { System.out.println("ExclusiveOrExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.XOR);  
			break;
 
    case 873 : if (DEBUG) { System.out.println("ExclusiveOrExpression_NotName ::= Name XOR AndExpression"); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.XOR);  
			break;
 
    case 875 : if (DEBUG) { System.out.println("InclusiveOrExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.OR);  
			break;
 
    case 876 : if (DEBUG) { System.out.println("InclusiveOrExpression_NotName ::= Name OR..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.OR);  
			break;
 
    case 878 : if (DEBUG) { System.out.println("ConditionalAndExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.AND_AND);  
			break;
 
    case 879 : if (DEBUG) { System.out.println("ConditionalAndExpression_NotName ::= Name AND_AND..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.AND_AND);  
			break;
 
    case 881 : if (DEBUG) { System.out.println("ConditionalOrExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeBinaryExpression(OperatorIds.OR_OR);  
			break;
 
    case 882 : if (DEBUG) { System.out.println("ConditionalOrExpression_NotName ::= Name OR_OR..."); }  //$NON-NLS-1$
		    consumeBinaryExpressionWithName(OperatorIds.OR_OR);  
			break;
 
    case 884 : if (DEBUG) { System.out.println("ConditionalExpression_NotName ::=..."); }  //$NON-NLS-1$
		    consumeConditionalExpression(OperatorIds.QUESTIONCOLON) ;  
			break;
 
    case 885 : if (DEBUG) { System.out.println("ConditionalExpression_NotName ::= Name QUESTION..."); }  //$NON-NLS-1$
		    consumeConditionalExpressionWithName(OperatorIds.QUESTIONCOLON) ;  
			break;
 
    case 889 : if (DEBUG) { System.out.println("AnnotationTypeDeclarationHeaderName ::= Modifiers AT..."); }  //$NON-NLS-1$
		    consumeAnnotationTypeDeclarationHeaderName() ;  
			break;
 
    case 890 : if (DEBUG) { System.out.println("AnnotationTypeDeclarationHeaderName ::= Modifiers AT..."); }  //$NON-NLS-1$
		    consumeAnnotationTypeDeclarationHeaderNameWithTypeParameters() ;  
			break;
 
    case 891 : if (DEBUG) { System.out.println("AnnotationTypeDeclarationHeaderName ::= AT..."); }  //$NON-NLS-1$
		    consumeAnnotationTypeDeclarationHeaderNameWithTypeParameters() ;  
			break;
 
    case 892 : if (DEBUG) { System.out.println("AnnotationTypeDeclarationHeaderName ::= AT..."); }  //$NON-NLS-1$
		    consumeAnnotationTypeDeclarationHeaderName() ;  
			break;
 
    case 893 : if (DEBUG) { System.out.println("AnnotationTypeDeclarationHeader ::=..."); }  //$NON-NLS-1$
		    consumeAnnotationTypeDeclarationHeader() ;  
			break;
 
    case 894 : if (DEBUG) { System.out.println("AnnotationTypeDeclaration ::=..."); }  //$NON-NLS-1$
		    consumeAnnotationTypeDeclaration() ;  
			break;
 
    case 896 : if (DEBUG) { System.out.println("AnnotationTypeMemberDeclarationsopt ::="); }  //$NON-NLS-1$
		    consumeEmptyAnnotationTypeMemberDeclarationsopt() ;  
			break;
 
    case 897 : if (DEBUG) { System.out.println("AnnotationTypeMemberDeclarationsopt ::= NestedType..."); }  //$NON-NLS-1$
		    consumeAnnotationTypeMemberDeclarationsopt() ;  
			break;
 
    case 899 : if (DEBUG) { System.out.println("AnnotationTypeMemberDeclarations ::=..."); }  //$NON-NLS-1$
		    consumeAnnotationTypeMemberDeclarations() ;  
			break;
 
    case 900 : if (DEBUG) { System.out.println("AnnotationMethodHeaderName ::= Modifiersopt Type..."); }  //$NON-NLS-1$
		    consumeMethodHeaderNameWithTypeParameters(true);  
			break;
 
    case 901 : if (DEBUG) { System.out.println("AnnotationMethodHeaderName ::= Modifiersopt Type..."); }  //$NON-NLS-1$
		    consumeMethodHeaderName(true);  
			break;
 
    case 902 : if (DEBUG) { System.out.println("AnnotationMethodHeaderDefaultValueopt ::="); }  //$NON-NLS-1$
		    consumeEmptyMethodHeaderDefaultValue() ;  
			break;
 
    case 903 : if (DEBUG) { System.out.println("AnnotationMethodHeaderDefaultValueopt ::= DefaultValue"); }  //$NON-NLS-1$
		    consumeMethodHeaderDefaultValue();  
			break;
 
    case 904 : if (DEBUG) { System.out.println("AnnotationMethodHeader ::= AnnotationMethodHeaderName..."); }  //$NON-NLS-1$
		    consumeMethodHeader();  
			break;
 
    case 905 : if (DEBUG) { System.out.println("AnnotationTypeMemberDeclaration ::=..."); }  //$NON-NLS-1$
		    consumeAnnotationTypeMemberDeclaration() ;  
			break;
 
    case 913 : if (DEBUG) { System.out.println("AnnotationName ::= AT Name"); }  //$NON-NLS-1$
		    consumeAnnotationName() ;  
			break;
 
    case 914 : if (DEBUG) { System.out.println("NormalAnnotation ::= AnnotationName LPAREN..."); }  //$NON-NLS-1$
		    consumeNormalAnnotation(false) ;  
			break;
 
    case 915 : if (DEBUG) { System.out.println("MemberValuePairsopt ::="); }  //$NON-NLS-1$
		    consumeEmptyMemberValuePairsopt() ;  
			break;
 
    case 918 : if (DEBUG) { System.out.println("MemberValuePairs ::= MemberValuePairs COMMA..."); }  //$NON-NLS-1$
		    consumeMemberValuePairs() ;  
			break;
 
    case 919 : if (DEBUG) { System.out.println("MemberValuePair ::= SimpleName EQUAL EnterMemberValue..."); }  //$NON-NLS-1$
		    consumeMemberValuePair() ;  
			break;
 
    case 920 : if (DEBUG) { System.out.println("EnterMemberValue ::="); }  //$NON-NLS-1$
		    consumeEnterMemberValue() ;  
			break;
 
    case 921 : if (DEBUG) { System.out.println("ExitMemberValue ::="); }  //$NON-NLS-1$
		    consumeExitMemberValue() ;  
			break;
 
    case 923 : if (DEBUG) { System.out.println("MemberValue ::= Name"); }  //$NON-NLS-1$
		    consumeMemberValueAsName() ;  
			break;
 
    case 926 : if (DEBUG) { System.out.println("MemberValueArrayInitializer ::=..."); }  //$NON-NLS-1$
		    consumeMemberValueArrayInitializer() ;  
			break;
 
    case 927 : if (DEBUG) { System.out.println("MemberValueArrayInitializer ::=..."); }  //$NON-NLS-1$
		    consumeMemberValueArrayInitializer() ;  
			break;
 
    case 928 : if (DEBUG) { System.out.println("MemberValueArrayInitializer ::=..."); }  //$NON-NLS-1$
		    consumeEmptyMemberValueArrayInitializer() ;  
			break;
 
    case 929 : if (DEBUG) { System.out.println("MemberValueArrayInitializer ::=..."); }  //$NON-NLS-1$
		    consumeEmptyMemberValueArrayInitializer() ;  
			break;
 
    case 930 : if (DEBUG) { System.out.println("EnterMemberValueArrayInitializer ::="); }  //$NON-NLS-1$
		    consumeEnterMemberValueArrayInitializer() ;  
			break;
 
    case 932 : if (DEBUG) { System.out.println("MemberValues ::= MemberValues COMMA MemberValue"); }  //$NON-NLS-1$
		    consumeMemberValues() ;  
			break;
 
    case 933 : if (DEBUG) { System.out.println("MarkerAnnotation ::= AnnotationName"); }  //$NON-NLS-1$
		    consumeMarkerAnnotation(false) ;  
			break;
 
    case 934 : if (DEBUG) { System.out.println("SingleMemberAnnotationMemberValue ::= MemberValue"); }  //$NON-NLS-1$
		    consumeSingleMemberAnnotationMemberValue() ;  
			break;
 
    case 935 : if (DEBUG) { System.out.println("SingleMemberAnnotation ::= AnnotationName LPAREN..."); }  //$NON-NLS-1$
		    consumeSingleMemberAnnotation(false) ;  
			break;
 
    case 940 : if (DEBUG) { System.out.println("RecoveryMethodHeaderName ::= Modifiersopt Type..."); }  //$NON-NLS-1$
		    consumeRecoveryMethodHeaderNameWithTypeParameters();  
			break;
 
    case 945 : if (DEBUG) { System.out.println("RecoveryMethodHeaderName ::= Modifiersopt Type..."); }  //$NON-NLS-1$
		    consumeRecoveryMethodHeaderName();  
			break;
 
    case 950 : if (DEBUG) { System.out.println("RecoveryMethodHeaderName ::= ModifiersWithDefault..."); }  //$NON-NLS-1$
		    consumeRecoveryMethodHeaderNameWithTypeParameters();  
			break;
 
    case 955 : if (DEBUG) { System.out.println("RecoveryMethodHeaderName ::= ModifiersWithDefault Type"); }  //$NON-NLS-1$
		    consumeRecoveryMethodHeaderName();  
			break;
 
    case 956 : if (DEBUG) { System.out.println("RecoveryMethodHeader ::= RecoveryMethodHeaderName..."); }  //$NON-NLS-1$
		    consumeMethodHeader();  
			break;
 
    case 957 : if (DEBUG) { System.out.println("RecoveryMethodHeader ::= RecoveryMethodHeaderName..."); }  //$NON-NLS-1$
		    consumeMethodHeader();  
			break;
 
	}
}
