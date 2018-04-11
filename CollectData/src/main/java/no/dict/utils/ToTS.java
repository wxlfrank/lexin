package no.dict.utils;

import java.lang.reflect.Field;

import no.dict.data.DictItem;

public class ToTS {
	public static String LINE = String.format("%n");
	public static String format = "export class %s {" + LINE + "%s}" + LINE + LINE;
	public static String field_format = "%s: %s;" + LINE;
	public static void main(String[] args) {
		toTSClass(DictItem.class);
	}
	
	public static void toTSClass(Class<?> clazz) {
		StringBuffer buffer = new StringBuffer();
		String name = clazz.getSimpleName();
		for(Field field : clazz.getDeclaredFields()) {
			String type = "";
			Class<?> field_type = field.getType();
			if(field.getType() == String.class) {
				type = "string";
			}else if(field_type == int.class){
				type = "number";
			}
			buffer.append(" " + String.format(field_format, field.getName(), type));
		}
		System.out.println(String.format(format, name, buffer.toString()));
	}

}
