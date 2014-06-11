package com.zeal.peekaboo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.penq.utils.BitmapUtils;
import com.zeal.peak.adapter.GalleryFilterAdapter;
import com.zeal.peak.adapter.GalleryImageAdapter;
import com.zeal.peak.utils.CommonUtils;

public class NextActivity extends Activity implements OnEditorActionListener {

	private static final String TAG = null;
	int LastVal = 0;
	private LinearLayout ll;
	private LinearLayout ll3;
	private Context context;
	private Bitmap bmp;
	public static Bitmap viewImage = null;
	public static Bitmap filterImage = null;
	private ImageView mImageView = null;
	public ImageView img;

	String urlTo = PeakAboo.BaseUrl + "/insertimage.php";
	String msgUrl = PeakAboo.BaseUrl + "/message_send.php?receiver_uid=";
	String selecteduri = "";
	ProgressDialog pDlg;

	String m_value = "";

	Boolean count = true, sum = true, fontclk = true;

	public BackIme edittext;
	public Gallery gallery;
	public Gallery gallery1;

	private int colorid = Color.WHITE;

	private Integer[] mImageIds = { R.color.spring_green, R.color.cyan,
			R.color.chartreuse, R.color.azure, R.color.blue,
			R.color.bright_pink, R.color.chartreuse, R.color.orange,
			R.color.violet, R.color.dark_green, R.color.grey, R.color.parrot };

	/*
	 * private Integer[] mFilterIds = { R.drawable.gamma, R.drawable.greyscale,
	 * R.drawable.sepia };
	 */
	private float X = 120;
	private float Y = 90;

	Boolean flag;

	int index;

	String[] mydata1 = { "Aleo Bold", "Bebas Neue", "Black out", "Capsuula",
			"Cookie Monster", "File", "Good Dog", "Good Intent", "Gota Light",
			"Gota", "HYPED", "Impact Label Black", "Impact Label White",
			"June Gloom", "Kabel", "Lovelo Black", "Lovelo Line Bold",
			"Metropolis 1920", "Moonshiner", "Myra", "Nexa Light", "Note This",
			"Nougatine", "Oswald Stencil", "Pipe", "Raleway Dots Regular",
			"Raleway Light", "VAL" };
	private Spinner spin;
	String c;

	Typeface font = null;

	private Button typetext;

	private SeekBar seek;

	private LinearLayout seekll;

	int sc_val;
	private String result;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_image);
		context = NextActivity.this;
		Bundle extras = getIntent().getExtras();
		String byteArray = extras.getString("picture");
		File f = new File(byteArray);

		bmp = decodeFile(f);
		spin = (Spinner) findViewById(R.id.spin);
		typetext = (Button) findViewById(R.id.font);
		typetext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				spin.performClick();

			}
		});

		pDlg = new ProgressDialog(context);

		mImageView = (ImageView) findViewById(R.id.image);
		mImageView.setImageBitmap(bmp);

		spin.setAdapter(new MyAdapter(context, mydata1));

		spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				System.err.println("**************" + arg2);

				switch (arg2) {
				case 0:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Aleo Bold.otf");
					// typetext.setBackgroundResource(R.drawable.atext);
					Saving(colorid);
					break;

				case 1:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Bebas Neue.otf");
					// typetext.setBackgroundResource(R.drawable.atext);
					Saving(colorid);
					break;
				case 2:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Blackout.ttf");
					// typetext.setBackgroundResource(R.drawable.atext);
					Saving(colorid);
					break;
				case 3:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Capsuula.ttf");
					// typetext.setBackgroundResource(R.drawable.atext);
					Saving(colorid);
					break;
				case 4:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Cookie Monster.ttf");
					// typetext.setBackgroundResource(R.drawable.atext);
					Saving(colorid);

					break;
				case 5:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/File.otf");
					// typetext.setBackgroundResource(R.drawable.atext);
					Saving(colorid);
					break;
				case 6:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Good Dog.otf");
					// typetext.setBackgroundResource(R.drawable.atext);
					Saving(colorid);
					break;
				case 7:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Good Intent.ttf");
					// typetext.setBackgroundResource(R.drawable.atext);
					Saving(colorid);
					break;

				case 8:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Gota Light.otf");
					Saving(colorid);
					break;

				case 9:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Gota.otf");
					Saving(colorid);
					break;
				case 10:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/HYPED.otf");
					Saving(colorid);
					break;
				case 11:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Impact Label Black.ttf");
					Saving(colorid);
					break;
				case 12:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Impact Label White.ttf");
					Saving(colorid);
					break;
				case 13:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/June Gloom.ttf");
					Saving(colorid);
					break;
				case 14:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Kabel.otf");
					Saving(colorid);
					break;
				case 15:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Lovelo Black.otf");
					Saving(colorid);
					break;
				case 16:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Lovelo Line Bold.otf");
					Saving(colorid);
					break;
				case 17:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Metropolis 1920.otf");
					Saving(colorid);
					break;
				case 18:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Moonshiner.otf");
					Saving(colorid);
					break;
				case 19:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Myra.otf");
					Saving(colorid);
					break;
				case 20:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Nexa Light.otf");
					Saving(colorid);
					break;

				case 21:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Note This.ttf");
					Saving(colorid);
					break;
				case 22:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Nougatine.ttf");
					Saving(colorid);
					break;
				case 23:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Oswald Stencil.otf");
					Saving(colorid);
					break;
				case 24:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Pipe.ttf");
					Saving(colorid);
					break;
				case 25:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Raleway Dots Regular.ttf");
					Saving(colorid);
					break;
				case 26:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Raleway Light.otf");
					Saving(colorid);
					break;
				case 27:

					font = Typeface.createFromAsset(getAssets(),
							"fonts/VAL.otf");
					Saving(colorid);
					break;

				default:

					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		mImageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {

				if (count) {
					edittext.setVisibility(View.GONE);
					gallery.setVisibility(View.GONE);
					createText();
					// CloseKeyBoard(context);
				}
				X = e.getX();
				Y = e.getY();
				System.err.println("X CORDINATE FOR CLICK " + X);

				Rect r = mImageView.getDrawable().getBounds();

				float drawLeft = r.left;
				float drawTop = r.top + 20;
				float drawRight = r.right - ((edittext.length() - 1) * 17);
				float drawBottom = r.bottom;
				System.err.println(drawRight);
				System.err.println(edittext.length());
				if (X > (drawRight)) {
					System.err.println("Invalid right pos");
				} else if (X < drawLeft) {
					System.err.println("Invalid Left pos");
				} else if (Y < drawTop) {
					System.err.println("Invalid Top pos");
				} else if (Y > drawBottom) {
					System.err.println("Invalid bottom pos");
				}

				else {
					Saving(colorid);
				}

				return true;
			}
		});

		ll = (LinearLayout) findViewById(R.id.LinearLayout01);
		ll3 = (LinearLayout) findViewById(R.id.LinearLayout03);
		// seekll=(LinearLayout)findViewById(R.id.seekll);
		ll3.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		ll3.setGravity(Gravity.CENTER_VERTICAL);

		edittext = new BackIme(context);
		edittext.setId(11);
		gallery = new Gallery(context);
		gallery.setId(21);
		gallery1 = new Gallery(context);
		gallery.setId(31);
		seek = new SeekBar(context);
		seek.setId(41);

		final DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);

		mImageView.getLayoutParams().height = (int) ((displayMetrics.heightPixels) / 1.75);
		mImageView.setImageBitmap(bmp);
		filterImage = bmp;
		img = new ImageView(context);

		if (count) {

			gallery.setVisibility(View.GONE);
			edittext.setVisibility(View.GONE);

			seek.setMax(100);
			// seekBar.setIndeterminate(true);

			ShapeDrawable thumb = new ShapeDrawable(new OvalShape());

			thumb.setIntrinsicHeight(20);
			thumb.setIntrinsicWidth(30);
			thumb.getPaint().setColor(Color.GRAY);
			seek.setThumb(thumb);
			seek.setProgress(1);
			seek.setMax(100);
			seek.setPadding(0, 0, 0, 20);

			seek.setVisibility(View.VISIBLE);
			seek.setBackgroundColor(Color.WHITE);

			LayoutParams lp = new LayoutParams(500, 30);
			seek.setLayoutParams(lp);

			// ll3.addView(seek);

			seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					Bundle extras = getIntent().getExtras();
					byte[] byteArray = extras.getByteArray("picture");

					Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0,
							byteArray.length);
					if (sc_val == 2) {
						bmp = doBrightness(bm, progress);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					}

				}
			});

			gallery1 = new Gallery(context);
			gallery1.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			gallery1.setGravity(Gravity.BOTTOM);

			gallery1.setSpacing(60);
			gallery1.setAdapter(new GalleryFilterAdapter(context));
			gallery1.setVisibility(View.VISIBLE);

			ll3.addView(gallery1);
			gallery1.setSelection(3);
			count = false;
			sum = true;
			gallery1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Toast.makeText(context, "Filter is--->" + arg2,
							Toast.LENGTH_SHORT).show();
					Bundle extras = getIntent().getExtras();
					String byteArray = extras.getString("picture");
					File f = new File(byteArray);

					Bitmap bm = decodeFile(f);
					if (arg2 == 4) {
						sc_val = 4;
						bmp = doGamma(bm, 2.8, 2.8, 2.8);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					} else if (arg2 == 1) {
						sc_val = 1;
						bmp = applySaturationFilter(bm, 2);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					} else if (arg2 == 2) {
						sc_val = 2;
						bmp = doBrightness(bm, 40);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					} else if (arg2 == 3) {
						sc_val = 3;
						bmp = createContrast(bm, 30);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);

					} else if (arg2 == 0) {
						sc_val = 0;
						bmp = doGreyscale(bm);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					} else {
						sc_val = 5;
						bmp = createSepiaToningEffect(bm, 4, 0.0, 7.8, 0.0);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					}

				}
			});

		}
	}

	class MyAdapter extends BaseAdapter {
		Context my_ctx;
		String[] fontsize;
		final int swidth;

		public MyAdapter(Context my_ctx, String[] fontsize) {
			super();
			this.my_ctx = my_ctx;
			this.fontsize = fontsize;
			final DisplayMetrics displayMetrics = new DisplayMetrics();
			((Activity) my_ctx).getWindowManager().getDefaultDisplay()
					.getMetrics(displayMetrics);
			swidth = displayMetrics.widthPixels;

		}

		@Override
		public int getCount() {
			return fontsize.length;
		}

		@Override
		public Object getItem(int position) {
			return fontsize[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {

			return getView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			try {
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li = (LayoutInflater) my_ctx
						.getSystemService(inflater);
				v = li.inflate(R.layout.myformat, parent, false);

				TextView tv_spinner_font = (TextView) v
						.findViewById(R.id.textView1);
				tv_spinner_font.setText(fontsize[position]);
				switch (position) {
				case 0:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Aleo Bold.otf");
					tv_spinner_font.setTypeface(font);
					break;

				case 1:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Bebas Neue.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 2:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Blackout.ttf");
					tv_spinner_font.setTypeface(font);
					break;
				case 3:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Capsuula.ttf");
					tv_spinner_font.setTypeface(font);
					break;
				case 4:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Cookie Monster.ttf");
					tv_spinner_font.setTypeface(font);

					break;
				case 5:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/File.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 6:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Good Dog.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 7:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Good Intent.ttf");
					tv_spinner_font.setTypeface(font);
					break;

				case 8:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Gota Light.otf");
					tv_spinner_font.setTypeface(font);
					break;

				case 9:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Gota.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 10:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/HYPED.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 11:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Impact Label Black.ttf");
					tv_spinner_font.setTypeface(font);
					break;
				case 12:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Impact Label White.ttf");
					tv_spinner_font.setTypeface(font);
					break;
				case 13:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/June Gloom.ttf");
					tv_spinner_font.setTypeface(font);
					break;
				case 14:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Kabel.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 15:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Lovelo Black.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 16:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Lovelo Line Bold.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 17:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Metropolis 1920.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 18:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Moonshiner.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 19:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Myra.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 20:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Nexa Light.otf");
					tv_spinner_font.setTypeface(font);
					break;

				case 21:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Note This.ttf");
					tv_spinner_font.setTypeface(font);
					break;
				case 22:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Nougatine.ttf");
					tv_spinner_font.setTypeface(font);
					break;
				case 23:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Oswald Stencil.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 24:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Pipe.ttf");
					tv_spinner_font.setTypeface(font);
					break;
				case 25:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Raleway Dots Regular.ttf");
					tv_spinner_font.setTypeface(font);
					break;
				case 26:
					font = Typeface.createFromAsset(getAssets(),
							"fonts/Raleway Light.otf");
					tv_spinner_font.setTypeface(font);
					break;
				case 27:

					font = Typeface.createFromAsset(getAssets(),
							"fonts/VAL.otf");
					tv_spinner_font.setTypeface(font);
					break;

				default:
					break;
				}

				tv_spinner_font
						.setBackgroundResource(R.drawable.setting_btn_bg);
				tv_spinner_font.setTextColor(Color.WHITE);
				tv_spinner_font.setGravity(Gravity.CENTER);

				tv_spinner_font.setTextSize(30);

			} catch (Exception e) {
				e.printStackTrace();
			}
			v.getLayoutParams().width = swidth;

			return v;
		}
	}

	public void createText(View v) {
		createText();
	}

	public void createText() {

		if (count) {

			gallery.setVisibility(View.GONE);
			edittext.setVisibility(View.GONE);
			CloseKeyBoard(context);
			seek = new SeekBar(context);
			ShapeDrawable thumb = new ShapeDrawable(new OvalShape());

			thumb.setIntrinsicHeight(20);
			thumb.setIntrinsicWidth(50);
			thumb.getPaint().setColor(Color.GRAY);
			seek.setThumb(thumb);
			seek.setProgress(1);
			seek.setMax(100);

			seek.setVisibility(View.VISIBLE);
			seek.setBackgroundColor(Color.WHITE);
			seek.setPadding(0, 0, 0, 20);

			LayoutParams lp = new LayoutParams(500, 30);
			seek.setLayoutParams(lp);

			// ll3.addView(seek);

			gallery1 = new Gallery(context);
			ll3.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
			ll3.setGravity(Gravity.CENTER_VERTICAL);
			gallery1.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));

			gallery1.setAdapter(new GalleryFilterAdapter(context));
			gallery1.setVisibility(View.VISIBLE);
			gallery1.setSpacing(60);
			// gallery1.setTop(20);

			ll3.addView(gallery1);
			gallery1.setSelection(3);
			count = false;
			sum = true;
			gallery1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Toast.makeText(context, "Filter is--->" + arg2,
							Toast.LENGTH_SHORT).show();
					Bundle extras = getIntent().getExtras();
					String byteArray = extras.getString("picture");
					File f = new File(byteArray);

					Bitmap bm = decodeFile(f);

					if (arg2 == 4) {

						bmp = doGamma(bm, 2.8, 2.8, 2.8);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					} else if (arg2 == 1) {

						bmp = applySaturationFilter(bm, 2);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					} else if (arg2 == 2) {
						bmp = doBrightness(bm, 40);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					} else if (arg2 == 3) {
						bmp = createContrast(bm, 30);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					} else if (arg2 == 0) {
						bmp = doGreyscale(bm);

						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					} else {

						bmp = createSepiaToningEffect(bm, 4, 0.0, 7.8, 0.0);
						mImageView.setImageBitmap(bmp);
						mImageView.setEnabled(false);
						Saving(colorid);
					}

				}
			});

			count = false;
		}

		else {
			mImageView.setEnabled(true);
			if (sum) {
				gallery1.setVisibility(View.GONE);
				seek.setVisibility(View.GONE);
				ll3.removeView(gallery);

				ll3.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				ll.removeView(edittext);

				InputFilter[] FilterArray = new InputFilter[1];
				FilterArray[0] = new InputFilter.LengthFilter(10);
				edittext.setFilters(FilterArray);
				edittext.setInputType(InputType.TYPE_CLASS_TEXT);
				edittext.setId(11);

				//

				gallery.setLayoutParams(new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				// gallery.setGravity(Gravity.FILL_VERTICAL);

				gallery.setAdapter(new GalleryImageAdapter(context));

				edittext.setVisibility(View.VISIBLE);

				gallery.scrollTo(20, 0);
				gallery.setVisibility(View.VISIBLE);
				gallery.setSpacing(2);
				// gallery.setTop(20);
				ll.addView(edittext);
				ll3.addView(gallery);
				gallery.setSelection(5);
				edittext.requestFocus();
				OpenKeyBoard(context);

				edittext.setOnEditTextImeBackListener(new EditTextImeBackListener() {

					@Override
					public void onImeBack(String text) {

						if (count) {
							edittext.setVisibility(View.GONE);
							gallery.setVisibility(View.GONE);
							createText();
							// CloseKeyBoard(context);
						}
					}
				});

				edittext.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							edittext.setVisibility(View.GONE);
							gallery.setVisibility(View.GONE);
							createText();
							// CloseKeyBoard(context);
						}
						return false;
					}
				});

				edittext.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						Saving(colorid);
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub

					}
				});

				count = true;

				gallery.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(context,
								"Color is--->" + mImageIds[arg2],
								Toast.LENGTH_SHORT).show();
						index = arg2;
						img.setBackgroundResource(mImageIds[arg2]);
						ColorDrawable imgcolor = (ColorDrawable) img
								.getBackground();
						colorid = imgcolor.getColor();
						mImageView.setEnabled(true);
						Saving(colorid);

					}
				});
			}
		}
	}

	public static Bitmap doGreyscale(Bitmap src) {
		// constant factors
		final double GS_RED = 0.299;
		final double GS_GREEN = 0.587;
		final double GS_BLUE = 0.114;

		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				src.getConfig());
		// pixel information
		int A, R, G, B;
		int pixel;

		// get image size
		int width = src.getWidth();
		int height = src.getHeight();

		// scan through every single pixel
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get one pixel color
				pixel = src.getPixel(x, y);
				// retrieve color of all channels
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// take conversion up to one single value
				R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	public static Bitmap createContrast(Bitmap src, double value) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int A, R, G, B;
		int pixel;
		// get contrast value
		double contrast = Math.pow((100 + value) / 100, 2);

		// scan through all pixels
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				// apply filter contrast for every channel R, G, B
				R = Color.red(pixel);
				R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (R < 0) {
					R = 0;
				} else if (R > 255) {
					R = 255;
				}

				G = Color.red(pixel);
				G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (G < 0) {
					G = 0;
				} else if (G > 255) {
					G = 255;
				}

				B = Color.red(pixel);
				B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (B < 0) {
					B = 0;
				} else if (B > 255) {
					B = 255;
				}

				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	public static Bitmap applySaturationFilter(Bitmap source, int level) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		float[] HSV = new float[3];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);

		int index = 0;
		// iteration through pixels
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// convert to HSV
				Color.colorToHSV(pixels[index], HSV);
				// increase Saturation level
				HSV[1] *= level;
				HSV[1] = (float) Math.max(0.0, Math.min(HSV[1], 1.0));
				// take color back
				pixels[index] |= Color.HSVToColor(HSV);
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}

	public static Bitmap createSepiaToningEffect(Bitmap src, int depth,
			double red, double green, double blue) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// constant grayscale
		final double GS_RED = 0.3;
		final double GS_GREEN = 0.59;
		final double GS_BLUE = 0.11;
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				// get color on each channel
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// apply grayscale sample
				B = G = R = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);

				// apply intensity level for sepid-toning on each channel
				R += (depth * red);
				if (R > 255) {
					R = 255;
				}

				G += (depth * green);
				if (G > 255) {
					G = 255;
				}

				B += (depth * blue);
				if (B > 255) {
					B = 255;
				}

				// set new pixel color to output image
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	public static Bitmap doGamma(Bitmap src, double red, double green,
			double blue) {
		// create output image
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				src.getConfig());
		// get image size
		int width = src.getWidth();
		int height = src.getHeight();
		// color information
		int A, R, G, B;
		int pixel;
		// constant value curve
		final int MAX_SIZE = 256;
		final double MAX_VALUE_DBL = 255.0;
		final int MAX_VALUE_INT = 255;
		final double REVERSE = 1.0;

		// gamma arrays
		int[] gammaR = new int[MAX_SIZE];
		int[] gammaG = new int[MAX_SIZE];
		int[] gammaB = new int[MAX_SIZE];

		// setting values for every gamma channels
		for (int i = 0; i < MAX_SIZE; ++i) {
			gammaR[i] = (int) Math.min(
					MAX_VALUE_INT,
					(int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE
							/ red)) + 0.5));
			gammaG[i] = (int) Math.min(
					MAX_VALUE_INT,
					(int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE
							/ green)) + 0.5));
			gammaB[i] = (int) Math.min(
					MAX_VALUE_INT,
					(int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE
							/ blue)) + 0.5));
		}

		// apply gamma table
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				// look up gamma
				R = gammaR[Color.red(pixel)];
				G = gammaG[Color.green(pixel)];
				B = gammaB[Color.blue(pixel)];
				// set new color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	public static Bitmap doBrightness(Bitmap src, int value) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);

				// increase/decrease each channel
				R += value;
				if (R > 255) {
					R = 255;
				} else if (R < 0) {
					R = 0;
				}

				G += value;
				if (G > 255) {
					G = 255;
				} else if (G < 0) {
					G = 0;
				}

				B += value;
				if (B > 255) {
					B = 255;
				} else if (B < 0) {
					B = 0;
				}

				// apply new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	public void canceltask(View v) {
		finish();
	}

	public void Done(View v) {
		/*
		 * mImageView.setDrawingCacheEnabled(true); Bitmap bitmap =
		 * mImageView.getDrawingCache();
		 * 
		 * String root = Environment.getExternalStorageDirectory().toString();
		 * File newDir = new File(root + "/saved_images"); newDir.mkdirs();
		 * Random gen = new Random(); int n = 10000; n = gen.nextInt(n); String
		 * fotoname = "photo-" + n + ".jpg"; File file = new File(newDir,
		 * fotoname); String s = file.getAbsolutePath();
		 * System.err.print("******************" + s); if (file.exists())
		 * file.delete(); try { FileOutputStream out = new
		 * FileOutputStream(file); bitmap.compress(Bitmap.CompressFormat.JPEG,
		 * 90, out); out.flush(); out.close();
		 * Toast.makeText(getApplicationContext(), "Saved to your folder ",
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * } catch (Exception e) {
		 * 
		 * }
		 */

		Bundle extras = getIntent().getExtras();
		c = extras.getString("Contact");
		int i = extras.getInt("Upldmsg");
		if (i == 2) {
			mImageView.setDrawingCacheEnabled(true);
			Bitmap bitmap = mImageView.getDrawingCache();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] bitmapdata = stream.toByteArray();
			ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
			messageupload mu = new messageupload();
			mu.exe(bs);

		} else if (i == 1) {

			mImageView.setDrawingCacheEnabled(true);
			Bitmap bitmap = mImageView.getDrawingCache();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();

			Intent intent = new Intent(this, AddCommentActivity.class);
			intent.putExtra("picture", byteArray);
			intent.putExtra("Media", "I");
			startActivity(intent);
			finish();
		}

	}

	public void Saving(int mImageIds2) {

		Bitmap mybmp;
		mybmp = bmp.copy(Bitmap.Config.ARGB_8888, true);

		// mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

		Canvas mCanvas;
		try {

			mCanvas = new Canvas(mybmp);
			// mCanvas.rotate(90);
			mCanvas.setBitmap(mybmp);

			String value = edittext.getText().toString();

			/*
			 * if(options.inSampleSize == 2){ mSize.width = 350; mSize.height =
			 * 200; }
			 * 
			 * int infoWindowOffsetX = (mSize.height-40);
			 * 
			 * RectF infoWindowRect = new RectF(0, infoWindowOffsetX,
			 * mSize.width-1, mSize.height-1);
			 * 
			 * Paint textBkgPaint = new Paint(); textBkgPaint.setARGB(225, 75,
			 * 75, 75); //gray textBkgPaint.setAntiAlias(true);
			 * 
			 * // Draw inner info window mCanvas.drawRoundRect(infoWindowRect,
			 * 5, 5, textBkgPaint);
			 * 
			 * /*Paint windowBorder = new Paint(); windowBorder.setARGB(255,
			 * 255, 255, 255); windowBorder.setAntiAlias(true);
			 * windowBorder.setStyle(Paint.Style.STROKE);
			 * windowBorder.setStrokeWidth(2);
			 * 
			 * // Draw border for info window
			 * mCanvas.drawRoundRect(infoWindowRect, 5, 5, windowBorder);
			 */

			Paint textPaint = new Paint();
			// Typeface font = getFonts();

			textPaint.setTextSize(70);

			textPaint.setColor(mImageIds2);
			textPaint.setTypeface(font);
			textPaint.setAntiAlias(true);

			// Draw trip name, text Size = 12

			mCanvas.drawText(value, 0, value.length(), X, Y, textPaint);

			// Draw image capture time
			Date time = new Date(System.currentTimeMillis());
			String capTime = time.toString();

			capTime = "Dt/Time: " + capTime;

			/*
			 * if(options.inSampleSize == 2){ capTime = capTime.substring(0,
			 * 28); }
			 */
			// mCanvas.drawText(capTime, 0, capTime.length(), 2 + 2, 100,
			// textPaint);

			// Draw album text
			/*
			 * String Album = "Album : " + "MyAlbum"; mCanvas.drawText(Album, 0,
			 * Album.length(), mSize.width-3, 100, speedPaint);
			 */

			this.viewImage = mybmp;
			filterImage = mybmp;
			showPicture();

		} catch (Exception e) {
			Log.e(TAG, "Capture Picture : Exception while drawing text on img"
					+ e.toString());
			bmp = bmp.copy(Bitmap.Config.RGB_565, false);
			this.viewImage = mybmp;
			filterImage = mybmp;
			showPicture();

		}

	}

	// for text
	void showPicture() {
		if (this.viewImage != null)
			mImageView.setImageBitmap(this.viewImage);

	}

	public void OpenKeyBoard(Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	// For close keyboard
	public void CloseKeyBoard(Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		Toast.makeText(getApplicationContext(), String.valueOf(keyCode),
				Toast.LENGTH_LONG).show();
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Toast.makeText(getApplicationContext(), "Zealous Sys",
					Toast.LENGTH_LONG).show();
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

	class messageupload extends AsyncTask<String, Integer, String> {
		ByteArrayInputStream bytestreams;
		private String m_prefUserID;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.e(TAG, "Selected Contacts are" + c);
			msgUrl = msgUrl + c;
			m_prefUserID = CommonUtils.getStringSharedPref(
					getApplicationContext(), "user_ID", "");
			String pic = "uid=" + m_prefUserID;
			BitmapUtils bu = new BitmapUtils();
			Log.e("hai", selecteduri);

			try {

				result = bu.multipartRequest(msgUrl, pic, bytestreams, "file",
						NextActivity.this);
				Log.e("result:", result);
			} catch (ParseException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			return result;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// pDlg.dismiss();
			Log.e("result:", result);
			if (result != null) {
				try {
					JSONObject jo = new JSONObject(result);
					Boolean b = jo.getBoolean("status");

					if (b) {
						AlertDialog.Builder ab = new AlertDialog.Builder(
								NextActivity.this);
						ab.setTitle("'Peak'a'Boo'");
						ab.setMessage("Message Sent Successfully");
						ab.setNeutralButton("ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										finish();
									}
								});
						ab.show();

					} else {
						AlertDialog.Builder ab = new AlertDialog.Builder(
								NextActivity.this);
						ab.setTitle("'Peak'a'Boo'");
						ab.setMessage("Error in Sending Picture!Try Later.");
						ab.setNeutralButton("ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										finish();
									}
								});
						ab.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}

		}

		public void exe(ByteArrayInputStream bs) {
			// TODO Auto-generated method stub
			bytestreams = bs;
			this.execute();
		}
	}

	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 512;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

}
