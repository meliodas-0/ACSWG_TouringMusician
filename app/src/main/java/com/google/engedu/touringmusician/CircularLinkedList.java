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


import android.graphics.Point;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CircularLinkedList implements Iterable<Point> {

    Set<Point> pointSet = new HashSet<>();

    private class Node {
        Point point;
        Node prev, next;

        Node(Point p, Node prev, Node next){
            pointSet.add(p);
            this.point = p;
            if(head == null){
                this.prev = this;
                this.next = this;
            }else{
                this.prev = prev;
                this.next = next;
                next.prev = this;
                prev.next = this;
            }
        }
    }

    Node head = null;

    public void insertBeginning(Point p) {

        head = new Node(p, (head==null)?null:head.prev , head);

    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        if(head == null || head == head.next)    return total;
        Node t = head.next;
        while(t != head){
            total += distanceBetween(t.point, t.prev.point);
            t = t.next;
        }
        total += distanceBetween(t.point, t.prev.point);
        return total;
    }

    public void insertNearest(Point p) {

        if(head == null || head == head.next){
            insertBeginning(p);
            return;
        }

        Node subs = head.next;
        Node t = null;
        float minDis = Float.MAX_VALUE;

        while(subs != head){
            float dis = distanceBetween(subs.point, p);
            if(dis < minDis){
                minDis = dis;
                t = subs;
            }
            subs = subs.next;
        }


        new Node(p, t, t.next);

    }

    public void insertSmallest(Point p) {

        if(head == null || head == head.next){
            insertBeginning(p);
            return;
        }

        Point n = null;
        float dist = Float.MAX_VALUE;

        for(Point s :  pointSet){
            float f = distanceBetween(p, s);
            if(f<dist){
                dist = f;
                n = s;
            }
        }

        Node t = head;

        do{

            if(t.point.equals(n)){
                break;
            }
            t = t.next;

        }while(t != head);

        float distntpp, distnptp;

        distntpp = distanceBetween(t.next.point, t.point) + distanceBetween(t.point, p) + distanceBetween(p, t.prev.point);
        distnptp = distanceBetween(t.next.point, p) + distanceBetween(t.point, p) + distanceBetween(t.point, t.prev.point);

        if(distnptp < distntpp){
            new Node(p, t, t.next);
        }else{
            new Node(p, t.prev, t);
        }

    }

    public void reset() {
        head = null;
        pointSet.clear();
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
