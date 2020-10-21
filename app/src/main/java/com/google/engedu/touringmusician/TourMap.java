/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list = new CircularLinkedList();
    private CircularLinkedList smallestList = new CircularLinkedList();
    private CircularLinkedList nearestList = new CircularLinkedList();
    private CircularLinkedList beginningList = new CircularLinkedList();
    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint linePaintOriginal = new Paint();
        linePaintOriginal.setColor(Color.BLACK);
        linePaintOriginal.setStrokeWidth(2);

        Paint bLinePaint = new Paint();
        bLinePaint.setColor(Color.RED);
        bLinePaint.setStrokeWidth(1);

        Paint nLinePaint = new Paint();
        nLinePaint.setColor(Color.BLUE);
        nLinePaint.setStrokeWidth(1);

        Paint sLinePaint = new Paint();
        sLinePaint.setColor(Color.GREEN);
        sLinePaint.setStrokeWidth(1);


        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        Point prev = null;

        //ORIGINAL PATH
        for (Point p : list) {
            if(prev != null)
                canvas.drawLine(prev.x, prev.y, p.x, p.y, linePaintOriginal);
            canvas.drawCircle(p.x, p.y, 20, pointPaint);
            prev = p;
        }
        if(prev != null){
            CircularLinkedList t = list;
            Point p = t.iterator().next();
            canvas.drawLine(prev.x, prev.y, p.x, p.y, linePaintOriginal);
        }

        //BEGINNING PATH
        prev = null;
        for (Point p : beginningList) {
            if(prev != null)
                canvas.drawLine(prev.x, prev.y, p.x, p.y, bLinePaint);
            prev = p;
        }
        if(prev != null){
            CircularLinkedList t = beginningList;
            Point p = t.iterator().next();
            canvas.drawLine(prev.x, prev.y, p.x, p.y, bLinePaint);
        }
        //NEAREST PATH
        prev = null;
        for (Point p : nearestList) {
            if(prev != null)
                canvas.drawLine(prev.x, prev.y, p.x, p.y, nLinePaint);
            prev = p;
        }
        if(prev != null){
            CircularLinkedList t = nearestList;
            Point p = t.iterator().next();
            canvas.drawLine(prev.x, prev.y, p.x, p.y, nLinePaint);
        }

        //SMALLEST PATH
        prev = null;
        for (Point p : smallestList) {
            if(prev != null)
                canvas.drawLine(prev.x, prev.y, p.x, p.y, sLinePaint);
            prev = p;
        }
        if(prev != null){
            CircularLinkedList t = smallestList;
            Point p = t.iterator().next();
            canvas.drawLine(prev.x, prev.y, p.x, p.y, sLinePaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
                if (insertMode.equals("Closest")) {
                    list.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list.insertSmallest(p);
                } else {
                    list.insertBeginning(p);
                }
                beginningList.insertBeginning(p);
                nearestList.insertNearest(p);
                smallestList.insertSmallest(p);
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                if (message != null) {
                    message.setText(String.format("Tour length is now %.2f(in black)\n" +
                                    "Beginning Tour Length is %.2f(in red)\n " +
                                    "Nearest Tour Length is %.2f(in blue)\n " +
                                    "Smallest Tour Length is %.2f(in green)",
                            list.totalDistance(),
                            beginningList.totalDistance(),
                            nearestList.totalDistance(),
                            smallestList.totalDistance()));
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list.reset();
        nearestList.reset();
        beginningList.reset();
        smallestList.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
