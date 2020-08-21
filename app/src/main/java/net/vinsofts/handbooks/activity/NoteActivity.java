package net.vinsofts.handbooks.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.common.Constant;
import net.vinsofts.handbooks.object.BookmarkObject;
import net.vinsofts.handbooks.sqlite.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HongChien on 26/04/2016.
 */
public class NoteActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etNote;
    private Button btnDoneNote, btnXoaNote;
    private ImageView imgBackNode;
    private boolean check = false;
    private Intent intent;
    private int temp;
    private RelativeLayout footer_note;
    private int pageDetailId;
    private List<BookmarkObject> listNote = new ArrayList<>();
    private boolean checkEdit = false;
    private int idPageDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_layout);
//        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        //TODO: change color when click button

        etNote = (EditText) findViewById(R.id.etNote);
        imgBackNode = (ImageView) findViewById(R.id.imgBackNode);
        btnDoneNote = (Button) findViewById(R.id.btnDoneNote);
        btnXoaNote = (Button) findViewById(R.id.btnXoaNote);
        footer_note = (RelativeLayout) findViewById(R.id.footer_note);

        imgBackNode.setOnClickListener(this);
        btnXoaNote.setOnClickListener(this);
        btnDoneNote.setOnClickListener(this);

        intent = getIntent();

        //Favorite Screen
        if (intent.getStringExtra("text") != null) {
            pageDetailId = intent.getIntExtra("pageDetailId", 0);
            etNote.setText(intent.getStringExtra("text"));
            temp = intent.getIntExtra("position", 0);
            check = true;
//            }
            //PageDetail
        } else {
            Database database = new Database(this);
            database.openDatabase();
            listNote = database.listBookmarkNote();
            database.closeDatabase();
            for (int i = 0; i < listNote.size(); i++) {
                if (intent.getIntExtra("ID_PAGEDETAIL", -1) == listNote.get(i).getPageDetailId()) {
                    etNote.setText(listNote.get(i).getDescription());
                }
            }
        }


    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.btnDoneNote:
                if (!check) {
                    //Note PageDetail
                    //rỗng k lưu
                    idPageDetail = intent.getIntExtra("ID_PAGEDETAIL", -1);
                    if (!etNote.getText().toString().equals("")) {

                        Database database = new Database(this);
                        database.openDatabase();
                        //Chen vao bookmark
                        for (int i = 0; i < listNote.size(); i++) {
                            //Neu da co trong favorite thi edit
                            if (idPageDetail == listNote.get(i).getPageDetailId()) {
                                database.updateBookmark(Constant.DESCRIPTION_COLUMN_BM, etNote.getText().toString(), Constant.ID_COLUMN_BM, listNote.get(i).getId());
                                checkEdit = true;
                            }
                        }

                        //Neu chua co trong favorite thi new item bookmark
                        if (!checkEdit) {
                            database.insertBookmark(
                                    0,
                                    idPageDetail,
                                    etNote.getText().toString());
                        }
                        database.closeDatabase();

                    } else {
                        Database database = new Database(this);
                        database.openDatabase();
                        database.deleteNoteBookmark(0, idPageDetail);
                        database.closeDatabase();
                    }

                } else {
                    //Favorite Fragment
                    //Nếu text rỗng => xóa item cũ
                    Intent intent = new Intent();
                    if (!etNote.getText().toString().equals("")) {
                        intent.putExtra("chien", etNote.getText().toString());
                        intent.putExtra("chienid", temp);
                        setResult(Constant.REQUEST_CODE_FAVORITE, intent);
                    } else {
                        //Chinh lai node neu de rong thi xoa luon khoi favorite
                        Database database = new Database(this);
                        database.openDatabase();
                        database.deleteNoteBookmark(0, pageDetailId);
                        database.closeDatabase();
                    }
                }


                imm.hideSoftInputFromWindow(btnDoneNote.getWindowToken(), 0);
                finish();
                break;
            case R.id.imgBackNode:

                imm.hideSoftInputFromWindow(imgBackNode.getWindowToken(), 0);
                finish();
                break;
            case R.id.btnXoaNote:
                etNote.setText("");
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
