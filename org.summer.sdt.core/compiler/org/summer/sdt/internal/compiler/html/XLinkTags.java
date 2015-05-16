package org.summer.sdt.internal.compiler.html;

import static org.summer.sdt.internal.compiler.lookup.TypeConstants.JAVA;
import static org.summer.sdt.internal.compiler.lookup.TypeConstants.LANG;

import org.summer.sdt.internal.compiler.util.HashtableOfCompoundName;

public class XLinkTags extends TagMapping {
	private final static  HashtableOfCompoundName tagMaps = new HashtableOfCompoundName();
	
	static{

		tagMaps.put("xlink".toCharArray(), new char[][]{JAVA, LANG, "TemplateSetting".toCharArray()});
		tagMaps.put("collection-template".toCharArray(), new char[][]{JAVA, LANG, "CollectionTemplateSetting".toCharArray()});
		tagMaps.put("fragment".toCharArray(), new char[][]{JAVA, LANG, "FragmentSetting".toCharArray()});

	}
	
	public char[][] getMappingClass(char[] name){
		char[][] result = tagMaps.get(name);
		return result;
	}
}
