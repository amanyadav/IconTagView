package net.androidsrc.tagview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import net.androidsrc.xTag.TagData;
import net.androidsrc.xTag.TagLayout;

public class MainActivity extends AppCompatActivity {

    TagLayout iconTagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iconTagView=(TagLayout)findViewById(R.id.iconTagView);
        iconTagView.setTags(getData());
        iconTagView.setOnTagClickListener(new TagLayout.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                Toast.makeText(MainActivity.this, tag+" Clicked!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TagData[] getData(){
        TagData[] data=new TagData[9];
        data[0]=new TagData("Person",R.drawable.ic_person_black_24dp);
        data[1]=new TagData("School",R.drawable.ic_school_black_24dp);
        data[2]=new TagData("Share This",R.drawable.ic_share_black_24dp);
        data[3]=new TagData("Aman",R.drawable.ic_person_black_24dp);
        data[4]=new TagData("Yadav",R.drawable.ic_school_black_24dp);
        data[5]=new TagData("Says",R.drawable.ic_share_black_24dp);
        data[6]=new TagData("Hello",R.drawable.ic_person_black_24dp);
        data[7]=new TagData("To",R.drawable.ic_school_black_24dp);
        data[8]=new TagData("All",R.drawable.ic_share_black_24dp);
        return data;
    }
}
