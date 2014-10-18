package com.tale.androidsqlhelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

/**
 * Created with IntelliJ IDEA. User: GIANG Date: 12/22/13 Time: 12:29 AM
 */
public class SQLiteHelper {

    private static final String TAG = SQLiteHelper.class.getSimpleName();

    public static String buildCreateTableStatement(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}

        final List<Field> fields = new ArrayList<Field>();

        final Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields != null && declaredFields.length > 0) {
            fields.addAll(Arrays.asList(declaredFields));
        }

        final Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            Field[] superFields = superclass.getDeclaredFields();
            if (superFields != null && superFields.length > 0) {
                fields.addAll(Arrays.asList(superFields));
            }
        }


        if (fields == null || fields.size() == 0) {
            return null;
        }


        final StringBuilder columnsBuilder = new StringBuilder("");
		for (int i = 0; i < fields.size(); i++) {
			final Field field = fields.get(i);
			final String columnDeclareString = buildColumnDeclareStatement(field);
			if (columnDeclareString != null) {
				columnsBuilder.append(columnDeclareString);
				columnsBuilder.append(",");
			}
		}
		if (columnsBuilder.length() == 0) {
			return null;
		}
		columnsBuilder.deleteCharAt(columnsBuilder.length() - 1);

		final StringBuilder createTableStatementBuilder = new StringBuilder("");
		createTableStatementBuilder.append("CREATE TABLE ");
		createTableStatementBuilder.append(clazz.getSimpleName());
		createTableStatementBuilder.append(" (");
		createTableStatementBuilder.append(columnsBuilder.toString());
		createTableStatementBuilder.append(" );");
        String createTableQuery = createTableStatementBuilder.toString();
        Log.d(TAG, String.format("Create table statement: \n%s", createTableQuery));
        return createTableQuery;
	}

	public static String buildDeleteTableStatement(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		return "DROP TABLE IF EXISTS " + clazz.getSimpleName();
	}
	
	public static ContentValues convert2ContentValues(Object object)
			throws IllegalArgumentException, IllegalAccessException {
		if (object == null) {
			return null;
		}

		final Class<?> clazz = object.getClass();
		final Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return null;
		}

		final ContentValues contentValues = new ContentValues();
		for (Field field : fields) {
            if (field.getName().equals("_id")) {
                continue;
            }
            field.setAccessible(true);
            final String type = field.getType().toString();
            if (type.equals("int") || type.contains("java.lang.Integer")) {
                contentValues.put(field.getName(), (Integer) field.get(object));
            } else if (type.equals("boolean")
                    || type.contains("java.lang.Boolean")) {
                contentValues.put(field.getName(), (Boolean) field.get(object));
            } else if (type.equals("long") || type.contains("java.lang.Long")) {
                contentValues.put(field.getName(), (Long) field.get(object));
            } else if (type.equals("float") || type.contains("java.lang.Float")) {
                contentValues.put(field.getName(), (Float) field.get(object));
            } else if (type.equals("double")
                    || type.contains("java.lang.Double")) {
                contentValues.put(field.getName(), (Double) field.get(object));
            } else if (type.contains("java.lang.String")) {
                contentValues.put(field.getName(), (String) field.get(object));
            }
        }

		return contentValues;
	}

	public static <T> T fromCursor(Cursor cursor, Class<T> desClass)
			throws InstantiationException, IllegalAccessException {
		if (cursor == null) {
			return null;
		}

		T result = desClass.newInstance();

		final Field[] fields = desClass.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return result;
		}

		for (Field field : fields) {
			field.setAccessible(true);
			final String type = field.getType().toString();
			final int index = cursor.getColumnIndex(field.getName());
			if (index == -1) {
				continue;
			}
			if (type.equals("int") || type.contains("java.lang.Integer")) {
				field.setInt(result, cursor.getInt(index));
			} else if (type.equals("boolean")
					|| type.contains("java.lang.Boolean")) {
				field.setBoolean(result, cursor.getInt(index) == 1);
			} else if (type.equals("long") || type.contains("java.lang.Long")) {
				field.setLong(result, cursor.getLong(index));
			} else if (type.equals("float") || type.contains("java.lang.Float")) {
				field.setFloat(result, cursor.getFloat(index));
			} else if (type.equals("double")
					|| type.contains("java.lang.Double")) {
				field.setDouble(result, cursor.getDouble(index));
			} else if (type.contains("java.lang.String")) {
				field.set(result, cursor.getString(index));
			}
		}

		return result;
	}

	private static String buildColumnDeclareStatement(Field field) {
		final String sqliteType = mapToSQLiteType(field.getType().toString());
		if (sqliteType != null) {
			final StringBuilder methodDeclareBuilder = new StringBuilder("");
			methodDeclareBuilder.append(field.getName());
			methodDeclareBuilder.append(" ");
			methodDeclareBuilder.append(sqliteType);
			if (field.getName().equals("_id")) {
				methodDeclareBuilder.append(" primary key autoincrement");
			}
			return methodDeclareBuilder.toString();
		}
		return null;
	}

	private static String mapToSQLiteType(String type) {
		if (type.equals("int") || type.contains("java.lang.Integer")
				|| type.equals("long") || type.contains("java.lang.Long")
				|| type.equals("boolean") || type.contains("java.lang.Boolean")) {
			return "INTEGER";
		} else if (type.equals("float") || type.contains("java.lang.Float")
				|| type.equals("double") || type.contains("java.lang.Double")) {
			return "REAL";
		} else if (type.contains("java.lang.String")) {
			return "TEXT";
		}
		return null;
	}
}
