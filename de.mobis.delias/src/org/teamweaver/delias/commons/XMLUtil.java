package org.teamweaver.delias.commons;

public class XMLUtil {
	
	public static final String STARTTAG = "<";

	public static final String ENDTAG = ">";

	public static final String FORWARD_SLASH = "/";

	public static final String EQUAL = "=";

	public static final String EMPTY_STRING = "";

	public static final String SINGLE_SPACE = " ";

	public static final String DOUBLE_QUOTE = "\"";

	public static final String TRUE_VALUE = "true";

	public static final String FALSE_VALUE = "false";
	
	public static final String ACTION_SEQUENCE_ELEMENT = "actions";

	public static final String SHELL_ELEMENT = "window";

	public static final String ACTION_ELEMENT = "action";

	public static final String HEADER_ELEMENT = "header";

	public static final String INCLUDE_ELEMENT = "include";

	public static final String OBJECTS_ELEMENT = "objects";

	public static final String OBJECT_ELEMENT = "object";

	public static final String ITEM_ELEMENT = "item";

	public static final String SELECT_ITEM_ELEMENT = "selected-item";

	public static final String PARENT_ELEMENT = "parent";

	/** XML Attributes */
	public static final String DESCRIPTIVE_ATTRIBUTE = "descriptive";

	public static final String TYPE_ATTRIBUTE = "operation";

	public static final String CONTEXT_ID_ATTRIBUTE = "contextCategoryId";

	public static final String WIDGET_ID_ATTRIBUTE = "widgetId";

	public static final String ID_ATTRIBUTE = "class_type";

	public static final String RETURN_CODE_ATTRIBUTE = "return";

	public static final String TIME_TO_WAIT_ATTRIBUTE = "wait";

	public static final String OUTPUT_ATTRIBUTE = "output";

	public static final String PATH_ATTRIBUTE = "path";

	public static final String REFERENCE_ID_ATTRIBUTE = "referenceId";

	public static final String RESOLVER_ATTRIBUTE = "parserId";

	public static final String VERSION_ATTRIBUTE = "version";

	public static final String STARTTIME_ATTRIBUTE = "start-time";

	public static final String ENDTIME_ATTRIBUTE = "end-time";

	public static final String X_COORD_ATTRIBUTE = "x-coord";

	public static final String Y_COORD_ATTRIBUTE = "y-coord";

	public static final String DETAIL_ATTRIBUTE = "detail";

	public static final String SELECTION_ATTRIBUTE = "selection";

	public static final String VALUE_ATTRIBUTE = "value";

	public static final String CHOICE_ID_ATTRIBUTE = "choiceId";

	public static final String LOCATION_ATTRIBUTE = "location";

	public static final String RESOURCE_ATTRIBUTE = "resource";

	public static final String HOOK_ATTRIBUTE = "hook";

	public static final String IS_CHAR_SET_ATTRIBUTE = "ischarset";
	
	public static void addElement(StringBuffer buffer, String element){
		System.out.println(element);
		buffer.append(STARTTAG);
		buffer.append(element);
		
	}
}
