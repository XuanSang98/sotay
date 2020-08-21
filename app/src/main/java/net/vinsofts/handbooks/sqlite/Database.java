package net.vinsofts.handbooks.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.vinsofts.handbooks.common.Constant;
import net.vinsofts.handbooks.object.BookmarkObject;
import net.vinsofts.handbooks.object.CategoryObject;
import net.vinsofts.handbooks.object.PageDetailObject;

/**
 * Created by AT on 4/20/2016.
 */
public class Database {
    private Context mContext;
    protected SQLiteDatabase sqLiteDatabase;
    private List<CategoryObject> list;
    private List<PageDetailObject> listPageDetail;
    private List<BookmarkObject> listBookmark;
    private List<BookmarkObject> listNote;


    private CategoryObject categoryObject;
    private PageDetailObject pageDetailObject;
    private BookmarkObject bookmarkObject;

    public Database(Context mContext) {
        this.mContext = mContext;
        writeData();
    }

    protected void writeData() {
        File fileDatabase = new File(Constant.PATH + "/" + Constant.DATABASE_NAME);
        if (!fileDatabase.exists()) {
            File file = new File(Constant.PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                fileDatabase.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(fileDatabase);
                InputStream inputStream = mContext.getAssets().open(Constant.DATABASE_NAME);
                byte[] bytes = new byte[1024];
                int count = inputStream.read(bytes);
                while (count != -1) {
                    fileOutputStream.write(bytes, 0, count);
                    count = inputStream.read(bytes);
                }
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void openDatabase() {
        sqLiteDatabase = mContext.openOrCreateDatabase(Constant.PATH + "/" + Constant.DATABASE_NAME, mContext.MODE_PRIVATE, null);
    }

    public void closeDatabase() {
        sqLiteDatabase.close();
    }

    public List<CategoryObject> listCateDb(String tableName) {
        list = new ArrayList<>();
        openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName + " where " + Constant.PARENT_ID_COLUMN_CATE + " = 4", null);
        cursor.moveToFirst();

        int indexId = cursor.getColumnIndex(Constant.ID_COLUMN_CATE);
        int indexParentId = cursor.getColumnIndex(Constant.PARENT_ID_COLUMN_CATE);
        int indexPercent = cursor.getColumnIndex(Constant.PERCENT_COMPLETE_COLUMN_CATE);
        int indexName = cursor.getColumnIndex(Constant.NAME_COLUMN_CATE);

        while (!cursor.isAfterLast()) {
            categoryObject = new CategoryObject();
            categoryObject.setId(cursor.getInt(indexId));
            categoryObject.setParentId(cursor.getInt(indexParentId));
            categoryObject.setPercentComplete(cursor.getInt(indexPercent));
            categoryObject.setName(cursor.getString(indexName));
            list.add(categoryObject);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return list;
    }

    public List<PageDetailObject> listPageDb(String tableName, int storyId) {
        listPageDetail = new ArrayList<>();
        openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName + " where " + Constant.STORY_ID_COLUMN_PD + " = " + storyId, null);
        cursor.moveToFirst();
        int indexIdPage = cursor.getColumnIndex(Constant.ID_COLUMN_PD);
        int indexStoryIdPage = cursor.getColumnIndex(Constant.STORY_ID_COLUMN_PD);
        int indexContentPage = cursor.getColumnIndex(Constant.CONTENT_COLUMN_PD);
        int indexPageIndexPage = cursor.getColumnIndex(Constant.PAGE_INDEX_COLUMN_PD);
        int indexFavoritePage = cursor.getColumnIndex(Constant.FAVORITE_COLUMN_PD);
        int indexCompletePage = cursor.getColumnIndex(Constant.COMPLETE_COLUMN_PD);

        while (!cursor.isAfterLast()) {
            pageDetailObject = new PageDetailObject();
            pageDetailObject.setId(cursor.getInt(indexIdPage));
            pageDetailObject.setStoryId(cursor.getInt(indexStoryIdPage));
            pageDetailObject.setContent(cursor.getString(indexContentPage));
            pageDetailObject.setPageIndex(cursor.getInt(indexPageIndexPage));
            pageDetailObject.setFavorite(cursor.getInt(indexFavoritePage));
            pageDetailObject.setComplete(cursor.getInt(indexCompletePage));
            listPageDetail.add(pageDetailObject);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return listPageDetail;
    }


    public List<PageDetailObject> listPageDetail() {
        listPageDetail = new ArrayList<>();
        openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constant.PAGE_DETAIL_TABLE, null);
        cursor.moveToFirst();
        int indexIdPage = cursor.getColumnIndex(Constant.ID_COLUMN_PD);
        int indexStoryIdPage = cursor.getColumnIndex(Constant.STORY_ID_COLUMN_PD);
        int indexContentPage = cursor.getColumnIndex(Constant.CONTENT_COLUMN_PD);
        int indexPageIndexPage = cursor.getColumnIndex(Constant.PAGE_INDEX_COLUMN_PD);
        int indexFavoritePage = cursor.getColumnIndex(Constant.FAVORITE_COLUMN_PD);
        int indexCompletePage = cursor.getColumnIndex(Constant.COMPLETE_COLUMN_PD);

        while (!cursor.isAfterLast()) {
            pageDetailObject = new PageDetailObject();
            pageDetailObject.setId(cursor.getInt(indexIdPage));
            pageDetailObject.setStoryId(cursor.getInt(indexStoryIdPage));
            pageDetailObject.setContent(cursor.getString(indexContentPage));
            pageDetailObject.setPageIndex(cursor.getInt(indexPageIndexPage));
            pageDetailObject.setFavorite(cursor.getInt(indexFavoritePage));
            pageDetailObject.setComplete(cursor.getInt(indexCompletePage));
            listPageDetail.add(pageDetailObject);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return listPageDetail;
    }

    public List<BookmarkObject> listBookmarkDB(String tableName) {
        listBookmark = new ArrayList<>();
        openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);
        cursor.moveToFirst();

        int indexIdBM = cursor.getColumnIndex(Constant.ID_COLUMN_BM);
        int indexStoryIdBM = cursor.getColumnIndex(Constant.STORY_COLUMN_BM);
        int indexPageDetailBM = cursor.getColumnIndex(Constant.PAGEDETAIL_ID_COLUMN_BM);
        int indexDesBM = cursor.getColumnIndex(Constant.DESCRIPTION_COLUMN_BM);

        while (!cursor.isAfterLast()) {
            bookmarkObject = new BookmarkObject();
            bookmarkObject.setId(cursor.getInt(indexIdBM));
            bookmarkObject.setStoryId(cursor.getInt(indexStoryIdBM));
            bookmarkObject.setPageDetailId(cursor.getInt(indexPageDetailBM));
            bookmarkObject.setDescription(cursor.getString(indexDesBM));
            listBookmark.add(bookmarkObject);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();

        return listBookmark;
    }

    public List<BookmarkObject> listBookmarkNote() {
        listNote = new ArrayList<>();
        openDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constant.BOOKMARK_TABLE +" where "+ Constant.STORY_COLUMN_BM+" =0", null);
        cursor.moveToFirst();

        int indexIdBM = cursor.getColumnIndex(Constant.ID_COLUMN_BM);
        int indexStoryIdBM = cursor.getColumnIndex(Constant.STORY_COLUMN_BM);
        int indexPageDetailBM = cursor.getColumnIndex(Constant.PAGEDETAIL_ID_COLUMN_BM);
        int indexDesBM = cursor.getColumnIndex(Constant.DESCRIPTION_COLUMN_BM);

        while (!cursor.isAfterLast()) {
            bookmarkObject = new BookmarkObject();
            bookmarkObject.setId(cursor.getInt(indexIdBM));
            bookmarkObject.setStoryId(cursor.getInt(indexStoryIdBM));
            bookmarkObject.setPageDetailId(cursor.getInt(indexPageDetailBM));
            bookmarkObject.setDescription(cursor.getString(indexDesBM));
            listNote.add(bookmarkObject);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();

        return listNote;
    }



    public void updatePageDetail(String tableName,String column, int value, int pageIndex, int storyId) {
        openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, value);
        sqLiteDatabase.update(tableName, contentValues, Constant.PAGE_INDEX_COLUMN_PD + " = " + pageIndex
                + " and " + Constant.STORY_ID_COLUMN_PD + " = " + storyId, null);
        closeDatabase();
    }
//
//    public void updateCompletePageDetail(int valueComplete, int pageIndex) {
//        openDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(Constant.COMPLETE_COLUMN_PD, valueComplete);
//        sqLiteDatabase.update(Constant.PAGE_DETAIL_TABLE, contentValues, Constant.PAGE_INDEX_COLUMN_PD + " = " + pageIndex, null);
//        closeDatabase();
//    }

    public void updateCategory(String tableName, int idCate, int value) {
        openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.PERCENT_COMPLETE_COLUMN_CATE, value);
        sqLiteDatabase.update(tableName, contentValues, Constant.ID_COLUMN_CATE + " = " + idCate, null);
        closeDatabase();
    }


    public void insertBookmark(int storyId, int pageId, String text) {
        openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.STORY_COLUMN_BM, storyId );
        contentValues.put(Constant.PAGEDETAIL_ID_COLUMN_BM, pageId);
        contentValues.put(Constant.DESCRIPTION_COLUMN_BM,text);
        sqLiteDatabase.insert(Constant.BOOKMARK_TABLE, null, contentValues);
        closeDatabase();
    }

    public void deleteBookmark(int id) {
        openDatabase();
        sqLiteDatabase.delete(Constant.BOOKMARK_TABLE, Constant.ID_COLUMN_BM + " = " + id, null);
        closeDatabase();
    }

    public void deleteNoteBookmark(int valueStoryId, int pageDetailId) {
        openDatabase();
        sqLiteDatabase.delete(Constant.BOOKMARK_TABLE, Constant.STORY_COLUMN_BM + " = " + valueStoryId +" and " + Constant.PAGEDETAIL_ID_COLUMN_BM+" = " +pageDetailId, null);
                closeDatabase();
    }



    public void updateBookmark(String columnValue, String value, String columnWhere, int id) {
        openDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(columnValue, value);
        sqLiteDatabase.update(Constant.BOOKMARK_TABLE, contentValues, columnWhere + " = " + id, null);
        closeDatabase();
    }

    public void updateFavoritePageDetail(int value, int idPage) {
        openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FAVORITE_COLUMN_PD, value);
        sqLiteDatabase.update(Constant.PAGE_DETAIL_TABLE, contentValues, Constant.ID_COLUMN_PD + " = " + idPage, null);
        closeDatabase();
    }

}
