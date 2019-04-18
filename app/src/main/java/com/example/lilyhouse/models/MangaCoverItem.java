package com.example.lilyhouse.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "manga_cover_item_table")
public class MangaCoverItem {

//	"id": 18473,
//	"name": "终将成为你",
//	"zone": "日本",
//	"status": "连载中",
//	"last_update_chapter_name": "第39话",
//	"last_update_chapter_id": 81761,
//	"last_updatetime": 1551318003,
//	"hidden": 0,
//	"cover": "webpic/16/zj0427l.jpg",
//	"first_letter": "z",
//	"comic_py": "zuizhongwochengleni",
//	"authors": "仲谷鳰(仲谷)",
//	"types": "百合",
//	"readergroup": "少年漫画",
//	"copyright": 0,
//	"hot_hits": 17410192,
//	"app_click_count": 16833475,
//	"num": "34243667"

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = 1;
    private static final String TAG = "======MangaCoverItem";
    public long jointimenanos;
    @SerializedName("last_updatetime")
    @Expose
    public long lastupdatetime;
    @SerializedName("app_click_count")
    @Expose
    public int appclickcount;
    @SerializedName("copyright")
    @Expose
    public int copyright;
    @SerializedName("hidden")
    @Expose
    public int hidden;
    @SerializedName("num")
    @Expose
    public int num;
    @SerializedName("hot_hits")
    @Expose
    public int hothits;
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("last_update_chapter_id")
    @Expose
    public int lastupdatechapterid;
    @SerializedName("types")
    @Expose
    public String types;
    @SerializedName("first_letter")
    @Expose
    public String firstletter;
    @SerializedName("readergroup")
    @Expose
    public String readergroup;
    @SerializedName("cover")
    @Expose
    public String cover;
    @SerializedName("comic_py")
    @Expose
    public String comicpy;
    @SerializedName("zone")
    @Expose
    public String zone;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("last_update_chapter_name")
    @Expose
    public String lastupdatechaptername;
    @SerializedName("authors")
    @Expose
    public String authors;
    @Ignore
    private int itemType = 0;

    public MangaCoverItem() {
        jointimenanos = System.nanoTime();
    }

    public MangaCoverItem(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}