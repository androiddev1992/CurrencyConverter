package com.currencyconverter.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.currencyconverter.data.DBHelper;
import com.currencyconverter.data.DatabaseContract;

public class ConversionProvider extends ContentProvider {

    private static final String TAG = ConversionProvider.class.getSimpleName();

    private static final int RATES = 200;
    private static final int RATES_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_CONVERSION_RATES,
                RATES);

        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_CONVERSION_RATES + "/#",
                RATES_WITH_ID);
    }

    private DBHelper mCardsDBHelper;

    @Override
    public boolean onCreate() {
        mCardsDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case RATES: {
                retCursor = mCardsDBHelper.getReadableDatabase().query(
                        DatabaseContract.TABLE_CONVERSION_RATES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case RATES_WITH_ID: {
                retCursor = mCardsDBHelper.getReadableDatabase().query(
                        DatabaseContract.TABLE_CONVERSION_RATES,
                        projection,
                        DatabaseContract.TableConversionRates.COL_ID + "=?",
                        new String[]{Long.toString(ContentUris.parseId(uri))}, null, null, sortOrder
                );

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mCardsDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RATES: {
                long id = db.insert(DatabaseContract.TABLE_CONVERSION_RATES, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("This provider does not support deletion");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("This provider does not support updates");
    }
}
