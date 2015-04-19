package org.summer.sdt.internal.compiler.html;

import static org.summer.sdt.internal.compiler.lookup.TypeConstants.*;

import java.util.HashMap;
import java.util.Map;

public final class LarkTags {
	public static final char[] LARK = "lark".toCharArray();
	
	private final static  Map<String, char[][]> tagMaps = new HashMap<String, char[][]>();
	static{

		tagMaps.put("template", new char[][]{JAVA, LANG, "TemplateSetting".toCharArray()});
		tagMaps.put("collection-template", new char[][]{JAVA, LANG, "CollectionTemplateSetting".toCharArray()});
		tagMaps.put("fragment", new char[][]{JAVA, LANG, "FragmentSetting".toCharArray()});

	}
	
	public static char[][] getMappingClass(char[] name){
		char[][] result = tagMaps.get(new String(name));
		return result;
	}
}
