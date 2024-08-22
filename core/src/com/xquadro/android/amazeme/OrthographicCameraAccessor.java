package com.xquadro.android.amazeme;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class OrthographicCameraAccessor implements TweenAccessor<OrthographicCamera> {
	public static final int POS_XY = 1;
	public static final int ZOOM = 2;
	public static final int POS_XY_ZOOM = 3;
	

	public int getValues(OrthographicCamera target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case POS_XY:
			returnValues[0] = target.position.x;
			returnValues[1] = target.position.y;
			return 2;
		case ZOOM:
			returnValues[0] = target.zoom;
			return 2;
		case POS_XY_ZOOM:
			returnValues[0] = target.position.x;
			returnValues[1] = target.position.y;
			returnValues[2] = target.zoom;
			return 3;
		default:
			assert false;
			return 0;
		}
	}

	public void setValues(OrthographicCamera target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case POS_XY:
			target.position.x = newValues[0];
			target.position.y = newValues[1];
			break;
		case ZOOM:
			target.zoom = newValues[0];
			break;
		case POS_XY_ZOOM:
			target.position.x = newValues[0];
			target.position.y = newValues[1];
			target.zoom = newValues[2];
			break;
		default:
			assert false;
			break;
		}
	}
}