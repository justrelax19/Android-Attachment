package com.example.anku.splashscreen;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.value;
import static android.provider.CalendarContract.CalendarCache.URI;

public class MainActivity extends AppCompatActivity {
    TextView value;
    EditText editTextEmail, editTextSubject, editTextMessage;
    Button btnSend, btnAttachment;
    String email, subject, message, attachmentFile;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;
    RelativeLayout abc;
    ImageButton img;
private TextInputLayout desc;
    private TextInputLayout title;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   btnAttachment = (Button) findViewById(R.id.buttonAttachment);
        //    btnSend = (Button) findViewById(R.id.buttonSend);
        value=(TextView) findViewById(R.id.txtView);
        //      btnSend.setOnClickListener(this);
        desc=(TextInputLayout) findViewById(R.id.input_layout_desc);
        title=(TextInputLayout) findViewById(R.id.input_layout_title);


        abc = (RelativeLayout) findViewById(R.id.relative);
        abc.setVisibility(View.GONE);

        img=(ImageButton) findViewById(R.id.Button01);
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                abc.setVisibility(View.GONE);
            }
        });


        Toolbar topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txt=(TextView)findViewById(R.id.textView);
        txt.setText("Your Requirements");
     //   getSupportActionBar().setTitle("Your Requirement");
     //   topToolBar.setLogo(R.drawable.ic_launcher);
      //  topToolBar.setLogoDescription(getResources().getString(R.string.message));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            /**
             * Get Path
             */
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            attachmentFile = cursor.getString(columnIndex);

            abc.setVisibility(View.VISIBLE);
            String filename=attachmentFile.substring(attachmentFile.lastIndexOf("/")+1);
            //   value.setText(filename);

            Log.e("Attachment Path:", attachmentFile);
            URI = Uri.parse("file://" + attachmentFile);


            File file = new File(attachmentFile);
            long length = file.length() / 1024; // Size in KB
            Log.e("length:",file.length()+"");
            value.setText(""+filename+" "+length+" "+"KB");

            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(attachmentFile));

            //   value.setBackground(getApplicationContext().getDrawable(R.drawable.grad));

            cursor.close();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.action_attach){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/* video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //intent.putExtra("return-data", true);
            if (URI != null) {
                intent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    PICK_FROM_GALLERY);

        }
        if(id == R.id.action_send){

//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Submitting Requirement")
//                    .setCancelable(false);
//
//            //Creating dialog box
//            AlertDialog alert = builder.create();
//            //Setting the title manually
//            alert.setTitle("AlertDialogExample");
//            alert.show();

            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Submitting Requirement...");
            dialog.setMessage("Please wait.");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();

            long delayInMillis = 5000;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, delayInMillis);
        }
        return super.onOptionsItemSelected(item);
    }
}
