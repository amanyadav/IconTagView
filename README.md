# CompassView

A custom view library to show categories of item which user can select one. This type of view is already used in apps like True Caller, Youtube and Gmail. Now you can also use this in your app.

## Screenshots

![Main screen][screen1]

## Features
* Random icon Background color generation 
* Customizeable background, padding, stroke etc. 
* Support for on click for each elements
* More to come..\m/

## Usage
Add this view in your layout file under some ViewGroup.
```xml
<net.androidsrc.xTag.TagLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/iconTagView"
        style="@style/TagLayout.Beauty_Red" />
```
Prepare data to be displayed in the TagLayout
```java
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
```
Now provide this data to TagLayout.
```java
TagLayout iconTagView=(TagLayout)findViewById(R.id.iconTagView);
          iconTagView.setTags(getData());
```
You can also set OnClickListener to the TagLayout.
```java
iconTagView.setOnTagClickListener(new TagLayout.OnTagClickListener() {
    @Override
    public void onTagClick(String tag) {
        Toast.makeText(MainActivity.this, tag+" Clicked!!", Toast.LENGTH_SHORT).show();
    }
});
```
## Note
You can fully customize layout by creating styles. Elements you can use are.
```xml
        <item name="net_borderColor">#49C120</item>
        <item name="net_textColor">#49C120</item>
        <item name="net_backgroundColor">#FFFFFF</item>
        <item name="net_randomIconBackgroundColor">true</item>
        <item name="net_iconBackgroundColor">#FF0000</item>
        <item name="net_borderStrokeWidth">0.5dp</item>
        <item name="net_circlePaddingFactor">1.0</item>
        <item name="net_iconPadding">6dp</item>
        <item name="net_textSize">15dp</item>
        <item name="net_horizontalSpacing">10dp</item>
        <item name="net_verticalSpacing">10dp</item>
        <item name="net_horizontalPadding">10dp</item>
        <item name="net_verticalPadding">10dp</item>
```
## Support Us
Please share and star this library to support us. 

## License

    This program is free software: you can redistribute it and/or modify it
    under the terms of the GNU General Public License as published by the Free
    Software Foundation, either version 3 of the License, or (at your option)
    any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
    more details.

    You should have received a copy of the GNU General Public License along
    with this program.  If not, see <http://www.gnu.org/licenses/>.
    
[screen1]: screenshots/shot1.png