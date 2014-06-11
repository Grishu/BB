package com.zeal.peekaboo;

import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageTwoInputFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.VideoView;

public class FilterActivity extends Activity implements
		OnSeekBarChangeListener, OnClickListener {
	final FilterList filters = new FilterList();
	private static final int REQUEST_PICK_IMAGE = 102;
	private static final int REQUEST_PICK_VIDEO = 103;
	private GPUImageFilter mFilter;
	private FilterAdjuster mFilterAdjuster;
	private GPUImageView mGPUImageView;
	AlertDialog.Builder dilogbuuilde;
	VideoView vedioveiw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filterfragment);
		addfilters();

		((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);
		findViewById(R.id.button_choose_filter).setOnClickListener(this);
		findViewById(R.id.button_save).setOnClickListener(this);
		vedioveiw = (VideoView) findViewById(R.id.vidoplayer);

		mGPUImageView = (GPUImageView) findViewById(R.id.gpuimage);

		dilogbuuilde = new AlertDialog.Builder(this);
		dilogbuuilde.setItems(new String[] { "Pick Image From Gallery",
				"Click Image with Camera", "Pick Video From Gallery",
				"Record Video with Camera" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							Intent photoPickerIntent = new Intent(
									Intent.ACTION_PICK);
							photoPickerIntent.setType("image/*");
							startActivityForResult(photoPickerIntent,
									REQUEST_PICK_IMAGE);
							// Toast.makeText(getApplicationContext(), "galery",
							// Toast.LENGTH_LONG).show();

							break;
						case 1:
							Intent intent = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							startActivityForResult(intent, REQUEST_PICK_IMAGE);
							// Toast.makeText(getApplicationContext(), "camera",
							// Toast.LENGTH_LONG).show();

							break;
						case 2:
							Intent intent2 = new Intent(Intent.ACTION_PICK,
									null);
							intent2.setType("video/*");
							startActivityForResult(intent2, REQUEST_PICK_VIDEO);
							// Toast.makeText(getApplicationContext(), "galery",
							// Toast.LENGTH_LONG).show();

							break;
						case 3:
							Intent takeVideoIntent = new Intent(
									MediaStore.ACTION_VIDEO_CAPTURE);
							takeVideoIntent.putExtra(
									MediaStore.EXTRA_DURATION_LIMIT, 20);
							startActivityForResult(takeVideoIntent,
									REQUEST_PICK_VIDEO);
							// Toast.makeText(getApplicationContext(), "camera",
							// Toast.LENGTH_LONG).show();

							break;

						default:
							Toast.makeText(getApplicationContext(),
									"Invalid Selection", Toast.LENGTH_LONG)
									.show();
							break;
						}

					}
				});
		dilogbuuilde.show();

	}

	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {

		switch (requestCode) {
		case REQUEST_PICK_IMAGE:
			if (resultCode == RESULT_OK) {
				handleImage(data.getData());
			} else {
				finish();
			}
			break;
		case REQUEST_PICK_VIDEO:
			if (resultCode == RESULT_OK) {
				handlevideo(data.getData());
			} else {
				finish();
			}
			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	private void handlevideo(Uri data) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), data.toString(),
				Toast.LENGTH_LONG).show();

		vedioveiw.setVideoURI(data);
		vedioveiw.start();

	}

	private void handleImage(final Uri selectedImage) {
		findViewById(R.id.vidio).setVisibility(View.GONE);
		mGPUImageView.setImage(selectedImage);
	}

	private void addfilters() {
		// TODO Auto-generated method stub
		filters.addFilter("Contrast", FilterType.CONTRAST);
		// filters.addFilter("Invert", FilterType.INVERT);
		// filters.addFilter("Pixelation", FilterType.PIXELATION);
		// filters.addFilter("Hue", FilterType.HUE);
		filters.addFilter("Gamma", FilterType.GAMMA);
		filters.addFilter("Brightness", FilterType.BRIGHTNESS);
		filters.addFilter("Sepia", FilterType.SEPIA);
		// filters.addFilter("Grayscale", FilterType.GRAYSCALE);
		// filters.addFilter("Sharpness", FilterType.SHARPEN);
		// filters.addFilter("Sobel Edge Detection",
		// FilterType.SOBEL_EDGE_DETECTION);
		// filters.addFilter("3x3 Convolution",
		// FilterType.THREE_X_THREE_CONVOLUTION);
		// filters.addFilter("Emboss", FilterType.EMBOSS);
		filters.addFilter("Posterize", FilterType.POSTERIZE);
		// filters.addFilter("Grouped filters", FilterType.FILTER_GROUP);
		filters.addFilter("Saturation", FilterType.SATURATION);
		// filters.addFilter("Exposure", FilterType.EXPOSURE);
		// filters.addFilter("Highlight Shadow", FilterType.HIGHLIGHT_SHADOW);
		// filters.addFilter("Monochrome", FilterType.MONOCHROME);
		// filters.addFilter("Opacity", FilterType.OPACITY);
		// filters.addFilter("RGB", FilterType.RGB);
		// filters.addFilter("White Balance", FilterType.WHITE_BALANCE);
		// filters.addFilter("Vignette", FilterType.VIGNETTE);
		// filters.addFilter("ToneCurve", FilterType.TONE_CURVE);
		/**/

		// filters.addFilter("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA);

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */
	private static GPUImageFilter createFilterForType(final Context context,
			final FilterType type) {
		switch (type) {
		case CONTRAST:
			return new GPUImageContrastFilter(2.0f);
		case GAMMA:
			return new GPUImageGammaFilter(2.0f);
		case INVERT:
			return new GPUImageColorInvertFilter();
		case PIXELATION:
			return new GPUImagePixelationFilter();
		case HUE:
			return new GPUImageHueFilter(90.0f);
		case BRIGHTNESS:
			return new GPUImageBrightnessFilter(1.5f);
		case GRAYSCALE:
			return new GPUImageGrayscaleFilter();
		case SEPIA:
			return new GPUImageSepiaFilter();
		case SHARPEN:
			GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
			sharpness.setSharpness(2.0f);
			return sharpness;
		case SOBEL_EDGE_DETECTION:
			return new GPUImageSobelEdgeDetection();

		case EMBOSS:
			return new GPUImageEmbossFilter();
		case POSTERIZE:
			return new GPUImagePosterizeFilter();
		case FILTER_GROUP:
			List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
			filters.add(new GPUImageContrastFilter());
			filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
			filters.add(new GPUImageGrayscaleFilter());
			return new GPUImageFilterGroup(filters);
		case SATURATION:
			return new GPUImageSaturationFilter(1.0f);
		case EXPOSURE:
			return new GPUImageExposureFilter(0.0f);
		case HIGHLIGHT_SHADOW:
			return new GPUImageHighlightShadowFilter(0.0f, 1.0f);
		case MONOCHROME:
			return new GPUImageMonochromeFilter(1.0f, new float[] { 0.6f,
					0.45f, 0.3f, 1.0f });
		case OPACITY:
			return new GPUImageOpacityFilter(1.0f);
		case RGB:
			return new GPUImageRGBFilter(1.0f, 1.0f, 1.0f);
		case WHITE_BALANCE:
			return new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
		case VIGNETTE:
			PointF centerPoint = new PointF();
			centerPoint.x = 0.5f;
			centerPoint.y = 0.5f;
			return new GPUImageVignetteFilter(centerPoint, new float[] { 0.0f,
					0.0f, 0.0f }, 0.3f, 0.75f);

		default:
			throw new IllegalStateException("No filter of that type!");
		}

	}

	private enum FilterType {
		CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION, SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, TONE_CURVE, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN, BLEND_DIFFERENCE, BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA, BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA
	}

	private static class FilterList {
		public List<String> names = new LinkedList<String>();
		public List<FilterType> filters = new LinkedList<FilterType>();

		public void addFilter(final String name, final FilterType filter) {
			names.add(name);
			filters.add(filter);
		}
	}

	private static GPUImageFilter createBlendFilter(Context context,
			Class<? extends GPUImageTwoInputFilter> filterClass) {
		try {
			GPUImageTwoInputFilter filter = filterClass.newInstance();
			filter.setBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_launcher));
			return filter;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class FilterAdjuster {
		private final Adjuster<? extends GPUImageFilter> adjuster;

		public FilterAdjuster(final GPUImageFilter filter) {
			if (filter instanceof GPUImageSharpenFilter) {
				adjuster = new SharpnessAdjuster().filter(filter);
			} else if (filter instanceof GPUImageSepiaFilter) {
				adjuster = null; // new SepiaAdjuster().filter(filter);
			} else if (filter instanceof GPUImageContrastFilter) {
				adjuster = new ContrastAdjuster().filter(filter);
			} else if (filter instanceof GPUImageGammaFilter) {
				adjuster = new GammaAdjuster().filter(filter);
			} else if (filter instanceof GPUImageBrightnessFilter) {
				adjuster = new BrightnessAdjuster().filter(filter);
			} else if (filter instanceof GPUImageSobelEdgeDetection) {
				adjuster = new SobelAdjuster().filter(filter);
			} else if (filter instanceof GPUImage3x3TextureSamplingFilter) {
				adjuster = new GPU3x3TextureAdjuster().filter(filter);
			} else if (filter instanceof GPUImageEmbossFilter) {
				adjuster = new EmbossAdjuster().filter(filter);
			} else if (filter instanceof GPUImageHueFilter) {
				adjuster = new HueAdjuster().filter(filter);
			} else if (filter instanceof GPUImagePosterizeFilter) {
				adjuster = new PosterizeAdjuster().filter(filter);
			} else if (filter instanceof GPUImagePixelationFilter) {
				adjuster = new PixelationAdjuster().filter(filter);
			} else if (filter instanceof GPUImageSaturationFilter) {
				adjuster = new SaturationAdjuster().filter(filter);
			} else if (filter instanceof GPUImageExposureFilter) {
				adjuster = new ExposureAdjuster().filter(filter);
			} else if (filter instanceof GPUImageHighlightShadowFilter) {
				adjuster = new HighlightShadowAdjuster().filter(filter);
			} else if (filter instanceof GPUImageMonochromeFilter) {
				adjuster = new MonochromeAdjuster().filter(filter);
			} else if (filter instanceof GPUImageOpacityFilter) {
				adjuster = new OpacityAdjuster().filter(filter);
			} else if (filter instanceof GPUImageRGBFilter) {
				adjuster = new RGBAdjuster().filter(filter);
			} else if (filter instanceof GPUImageWhiteBalanceFilter) {
				adjuster = new WhiteBalanceAdjuster().filter(filter);
			} else if (filter instanceof GPUImageVignetteFilter) {
				adjuster = new VignetteAdjuster().filter(filter);
			} else if (filter instanceof GPUImageDissolveBlendFilter) {
				adjuster = new DissolveBlendAdjuster().filter(filter);
			} else {
				adjuster = null;
			}
		}

		public void adjust(final int percentage) {
			if (adjuster != null) {
				adjuster.adjust(percentage);
			}
		}

		private abstract class Adjuster<T extends GPUImageFilter> {
			private T filter;

			@SuppressWarnings("unchecked")
			public Adjuster<T> filter(final GPUImageFilter filter) {
				this.filter = (T) filter;
				return this;
			}

			public T getFilter() {
				return filter;
			}

			public abstract void adjust(int percentage);

			protected float range(final int percentage, final float start,
					final float end) {
				return (end - start) * percentage / 100.0f + start;
			}

			protected int range(final int percentage, final int start,
					final int end) {
				return (end - start) * percentage / 100 + start;
			}
		}

		private class SharpnessAdjuster extends Adjuster<GPUImageSharpenFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setSharpness(range(percentage, -4.0f, 4.0f));
			}
		}

		private class PixelationAdjuster extends
				Adjuster<GPUImagePixelationFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setPixel(range(percentage, 1.0f, 100.0f));
			}
		}

		private class HueAdjuster extends Adjuster<GPUImageHueFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setHue(range(percentage, 0.0f, 360.0f));
			}
		}

		private class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setContrast(range(percentage, 0.0f, 2.0f));
			}
		}

		private class GammaAdjuster extends Adjuster<GPUImageGammaFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setGamma(range(percentage, 0.0f, 3.0f));
			}
		}

		private class BrightnessAdjuster extends
				Adjuster<GPUImageBrightnessFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setBrightness(range(percentage, -1.0f, 1.0f));
			}
		}

		private class SepiaAdjuster extends Adjuster<GPUImageSepiaFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setIntensity(range(percentage, 0.0f, 2.0f));
			}
		}

		private class SobelAdjuster extends
				Adjuster<GPUImageSobelEdgeDetection> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setLineSize(range(percentage, 0.0f, 5.0f));
			}
		}

		private class EmbossAdjuster extends Adjuster<GPUImageEmbossFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setIntensity(range(percentage, 0.0f, 4.0f));
			}
		}

		private class PosterizeAdjuster extends
				Adjuster<GPUImagePosterizeFilter> {
			@Override
			public void adjust(final int percentage) {
				// In theorie to 256, but only first 50 are interesting
				getFilter().setColorLevels(range(percentage, 1, 50));
			}
		}

		private class GPU3x3TextureAdjuster extends
				Adjuster<GPUImage3x3TextureSamplingFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setLineSize(range(percentage, 0.0f, 5.0f));
			}
		}

		private class SaturationAdjuster extends
				Adjuster<GPUImageSaturationFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setSaturation(range(percentage, 0.0f, 2.0f));
			}
		}

		private class ExposureAdjuster extends Adjuster<GPUImageExposureFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setExposure(range(percentage, -10.0f, 10.0f));
			}
		}

		private class HighlightShadowAdjuster extends
				Adjuster<GPUImageHighlightShadowFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setShadows(range(percentage, 0.0f, 1.0f));
				getFilter().setHighlights(range(percentage, 0.0f, 1.0f));
			}
		}

		private class MonochromeAdjuster extends
				Adjuster<GPUImageMonochromeFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setIntensity(range(percentage, 0.0f, 1.0f));
				// getFilter().setColor(new float[]{0.6f, 0.45f, 0.3f, 1.0f});
			}
		}

		private class OpacityAdjuster extends Adjuster<GPUImageOpacityFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setOpacity(range(percentage, 0.0f, 1.0f));
			}
		}

		private class RGBAdjuster extends Adjuster<GPUImageRGBFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setRed(range(percentage, 0.0f, 1.0f));
				// getFilter().setGreen(range(percentage, 0.0f, 1.0f));
				// getFilter().setBlue(range(percentage, 0.0f, 1.0f));
			}
		}

		private class WhiteBalanceAdjuster extends
				Adjuster<GPUImageWhiteBalanceFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setTemperature(range(percentage, 2000.0f, 8000.0f));
				// getFilter().setTint(range(percentage, -100.0f, 100.0f));
			}
		}

		private class VignetteAdjuster extends Adjuster<GPUImageVignetteFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setVignetteStart(range(percentage, 0.0f, 1.0f));
			}
		}

		private class DissolveBlendAdjuster extends
				Adjuster<GPUImageDissolveBlendFilter> {
			@Override
			public void adjust(final int percentage) {
				getFilter().setMix(range(percentage, 0.0f, 1.0f));
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		AlertDialog.Builder ab = new AlertDialog.Builder(FilterActivity.this);
		ab.setTitle("choose Filter");
		ab.setItems(filters.names.toArray(new String[filters.names.size()]),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						switchFilterTo(createFilterForType(FilterActivity.this,
								filters.filters.get(arg1)));
						mGPUImageView.requestRender();

					}
				});
		ab.show();

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (mFilterAdjuster != null) {
			mFilterAdjuster.adjust(progress);
		}
		mGPUImageView.requestRender();

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	private void switchFilterTo(final GPUImageFilter filter) {
		if (mFilter == null
				|| (filter != null && !mFilter.getClass().equals(
						filter.getClass()))) {
			mFilter = filter;
			mGPUImageView.setFilter(mFilter);
			mFilterAdjuster = new FilterAdjuster(mFilter);
		}
	}
}
