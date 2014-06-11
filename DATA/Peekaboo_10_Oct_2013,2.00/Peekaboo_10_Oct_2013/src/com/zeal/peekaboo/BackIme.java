package com.zeal.peekaboo;



import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;


public class BackIme extends EditText {

	 private EditTextImeBackListener mOnImeBack;
	   public BackIme(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	   public BackIme(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
		  mOnImeBack.onImeBack( this.getText().toString());
		}
		return super.onKeyPreIme(keyCode, event);
	}
	
	   public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
	        mOnImeBack = listener;
	    }

}
interface EditTextImeBackListener {

  /**
   * Se produit lorsque la touche Back est pressé pour sortir du mode clavier de l'IME
   * @param ctrl Contrôle EditTextBackEvent parent
   * @param text Texte du contrôle
   */
  public abstract void onImeBack( String text);
}
