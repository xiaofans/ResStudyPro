package com.j256.ormlite.db;

import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.FieldConverter;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.types.DateStringType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import java.sql.SQLException;

public class SqliteAndroidDatabaseType extends BaseSqliteDatabaseType {
    public void loadDriver() {
    }

    public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
        return true;
    }

    protected String getDriverClassName() {
        return null;
    }

    public String getDatabaseName() {
        return "Android SQLite";
    }

    protected void appendDateType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        appendStringType(sb, fieldType, fieldWidth);
    }

    protected void appendBooleanType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        appendShortType(sb, fieldType, fieldWidth);
    }

    public FieldConverter getFieldConverter(DataPersister dataPersister) {
        switch (dataPersister.getSqlType()) {
            case DATE:
                return DateStringType.getSingleton();
            default:
                return super.getFieldConverter(dataPersister);
        }
    }

    public boolean isNestedSavePointsSupported() {
        return false;
    }

    public boolean isBatchUseTransaction() {
        return true;
    }

    public <T> DatabaseTableConfig<T> extractDatabaseTableConfig(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
        return DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
    }
}
