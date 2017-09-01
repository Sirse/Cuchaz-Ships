/*******************************************************************************
 * Copyright (c) 2013 Jeff Martin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jeff Martin - initial API and implementation
 ******************************************************************************/
package cuchaz.modsShared.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cuchaz.modsShared.math.CompareReal;
import cuchaz.modsShared.math.HashCalculator;

public class CircleRange implements Comparable<CircleRange>, Serializable {
	
	private static final long serialVersionUID = 3905814480659132660L;
	
	/*********************************
	 * Definitions
	 *********************************/
	
	private static final double TwoPi = Math.PI * 2.0;
	
	/*********************************
	 * Data Members
	 *********************************/
	
	private double m_offset; // in the range [-pi,pi]
	private double m_length; // length is always counterclockwise
	private boolean m_isCircle;
	
	/*********************************
	 * Constructors
	 *********************************/
	
	private CircleRange() {
		// use the static factory methods
	}
	
	public CircleRange(CircleRange other) {
		m_offset = other.m_offset;
		m_length = other.m_length;
		m_isCircle = other.m_isCircle;
	}
	
	/*********************************
	 * Accessors
	 *********************************/
	
	public double getSource() {
		return m_offset;
	}
	
	public double getTarget() {
		return mapMinusPiToPi(m_offset + m_length);
	}
	
	public double getMidpoint() {
		return mapMinusPiToPi(m_offset + m_length / 2.0);
	}
	
	public boolean isCircle() {
		return m_isCircle;
	}
	
	public double getLength() {
		return m_length;
	}
	
	public boolean includesPi() {
		return CompareReal.gte(m_offset + m_length, Math.PI);
	}
	
	/*********************************
	 * Static Methods
	 *********************************/
	
	public static CircleRange newByPoint(double point) {
		CircleRange range = new CircleRange();
		range.m_offset = mapMinusPiToPi(point);
		range.m_length = 0;
		range.m_isCircle = false;
		return range;
	}
	
	public static CircleRange newByOffset(double offset, double length) {
		CircleRange range = new CircleRange();
		range.m_offset = mapMinusPiToPi(offset);
		range.m_length = Math.min(length, TwoPi);
		range.m_isCircle = length >= TwoPi;
		return range;
	}
	
	public static CircleRange newByShortSegment(double start, double stop) {
		// NOTE: the orientation of the segment is unknown, so choose the shorter of the two possibilities
		
		start = mapMinusPiToPi(start);
		stop = mapMinusPiToPi(stop);
		
		// sort the points
		double max = 0.0;
		double min = 0.0;
		if (start > stop) {
			max = start;
			min = stop;
		} else if (stop > start) {
			max = stop;
			min = start;
		} else {
			// assume it's a point interval instead of the whole circle
			return newByPoint(start);
		}
		
		// check the "sense" direction first
		CircleRange range = new CircleRange();
		range.m_isCircle = false;
		if (max - min < Math.PI) {
			range.m_offset = min;
			range.m_length = max - min;
		}
		// otherwise, use the "antisense" direction
		else {
			range.m_offset = max;
			range.m_length = Math.PI * 2.0 - (max - min);
		}
		return range;
	}
	
	public static CircleRange newByCounterclockwiseSegment(double source, double target) {
		source = mapMinusPiToPi(source);
		target = mapMinusPiToPi(target);
		
		CircleRange range = new CircleRange();
		range.m_offset = source;
		if (target >= source) {
			range.m_length = target - source;
		} else {
			range.m_length = 2.0 * Math.PI + (target - source);
		}
		range.m_isCircle = false;
		return range;
	}
	
	public static CircleRange newByThreePoints(double source, double target, double interior) {
		source = mapMinusPiToPi(source);
		target = mapMinusPiToPi(target);
		interior = mapMinusPiToPi(interior);
		
		CircleRange range = null;
		if (source == target) {
			range = new CircleRange();
			if (source == interior) {
				// it's a point
				range.m_isCircle = false;
				range.m_offset = source;
				range.m_length = 0;
			} else {
				// it's a circle
				range.m_isCircle = true;
				range.m_offset = source;
				range.m_length = TwoPi;
			}
		} else {
			// pick an order arbitrarily
			range = newByCounterclockwiseSegment(source, target);
			
			// if it's wrong, pick the other order
			if (!range.containsPoint(interior)) {
				range = newByCounterclockwiseSegment(target, source);
			}
			
			assert (range.containsPoint(interior));
		}
		return range;
	}
	
	public static CircleRange newCircle() {
		CircleRange range = new CircleRange();
		range.setCircle();
		return range;
	}
	
	public static double mapMinusPiToPi(double angle) {
		while (angle > Math.PI) {
			angle -= TwoPi;
		}
		while (angle <= -Math.PI) {
			angle += TwoPi;
		}
		return angle;
	}
	
	public static double mapZeroToTwoPi(double angle) {
		while (angle >= TwoPi) {
			angle -= TwoPi;
		}
		while (angle < 0) {
			angle += TwoPi;
		}
		return angle;
	}
	
	public static float mapZeroTo360(float angle) {
		while (angle >= 360.0f) {
			angle -= 360.0f;
		}
		while (angle < 0) {
			angle += 360.0f;
		}
		return angle;
	}
	
	public static boolean isEq(double a, double b) {
		return isEq(a, b, CompareReal.getEpsilon());
	}
	
	public static boolean isEq(double a, double b, double epsilon) {
		a = mapZeroToTwoPi(a);
		b = mapZeroToTwoPi(b);
		
		return CompareReal.eq(a, b, epsilon) || (CompareReal.eq(a, 0.0, epsilon) && CompareReal.eq(b, TwoPi, epsilon)) || (CompareReal.eq(b, 0.0, epsilon) && CompareReal.eq(a, TwoPi, epsilon));
	}
	
	/*********************************
	 * Methods
	 *********************************/
	
	public void merge(CircleRange other) {
		// is either one of us a circle?
		if (m_isCircle) {
			return;
		} else if (other.m_isCircle) {
			setCircle();
		}
		
		// do they even intersect?
		if (!isIntersecting(other)) {
			throw new IllegalArgumentException("The two ranges do not intersect!");
		}
		
		// rotate to a coord system where our offset is always zero
		double otherOffset = mapZeroToTwoPi(other.m_offset - m_offset);
		
		// expand our range
		if (CompareReal.lte(otherOffset, m_length)) {
			// update just the length
			m_length = Math.max(m_length, other.m_length + otherOffset);
			
			// did we make a circle?
			if (m_length > TwoPi) {
				setCircle();
			}
		} else {
			double upstreamDiff = TwoPi - otherOffset;
			double downstreamDiff = other.m_length - upstreamDiff - m_length;
			
			m_length += upstreamDiff + Math.max(downstreamDiff, 0.0);
			m_offset = mapMinusPiToPi(m_offset - upstreamDiff);
		}
	}
	
	public boolean isIntersecting(CircleRange other) {
		if (m_isCircle || other.m_isCircle) {
			return true;
		}
		
		return containsPoint(other.getSource()) || containsPoint(other.getTarget()) || other.containsPoint(getSource()) || other.containsPoint(getTarget());
	}
	
	public boolean isIntersectingOnlyOnBoundary(CircleRange other) {
		if (m_isCircle || other.m_isCircle) {
			return false;
		}
		
		if (CompareReal.eq(getTarget(), other.getSource())) {
			return m_length == 0 || other.m_length == 0 || !containsPoint(other.getTarget()) || !other.containsPoint(getSource());
		} else if (CompareReal.eq(getSource(), other.getTarget())) {
			return m_length == 0 || other.m_length == 0 || !containsPoint(other.getSource()) || !other.containsPoint(getTarget());
		}
		return false;
	}
	
	public boolean containsPoint(double point) {
		return containsPoint(point, CompareReal.getEpsilon());
	}
	
	public boolean containsPoint(double point, double epsilon) {
		if (m_isCircle) {
			return true;
		}
		
		// rotate to a coord system where our offset is zero
		point = mapZeroToTwoPi(mapZeroToTwoPi(point) - mapZeroToTwoPi(m_offset));
		return (CompareReal.lte(point, m_length, epsilon) && CompareReal.gte(point, 0.0, epsilon)) || CompareReal.eq(point, TwoPi, epsilon);
	}
	
	public boolean containsPointOnBoundary(double point) {
		return containsPointOnBoundary(point, CompareReal.getEpsilon());
	}
	
	public boolean containsPointOnBoundary(double point, double epsilon) {
		if (m_isCircle) {
			return false;
		}
		
		// rotate to a coord system where our offset is zero
		point = mapZeroToTwoPi(mapZeroToTwoPi(point) - mapZeroToTwoPi(m_offset));
		return CompareReal.eq(point, m_length, epsilon) || CompareReal.eq(point, 0.0, epsilon) || CompareReal.eq(point, TwoPi, epsilon);
	}
	
	public boolean containsPointOnInterior(double point) {
		return containsPointOnInterior(point, CompareReal.getEpsilon());
	}
	
	public boolean containsPointOnInterior(double point, double epsilon) {
		if (m_isCircle) {
			return true;
		}
		
		return containsPoint(point, epsilon) && !containsPointOnBoundary(point, epsilon);
	}
	
	@Override
	public int compareTo(CircleRange other) {
		return Double.compare(m_offset, other.m_offset);
	}
	
	@Override
	public boolean equals(Object other) {
		if (! (other instanceof CircleRange)) {
			return false;
		}
		return equals((CircleRange)other);
	}
	
	public boolean equals(CircleRange other) {
		// handle the circles cases
		if (m_isCircle && other.m_isCircle) {
			return true;
		} else if (m_isCircle != other.m_isCircle) {
			return false;
		}
		
		// both not circles at this point
		return m_offset == other.m_offset && m_length == other.m_length;
	}
	
	public boolean approximatelyEquals(CircleRange other) {
		return approximatelyEquals(other, CompareReal.getEpsilon());
	}
	
	public boolean approximatelyEquals(CircleRange other, double epsilon) {
		// handle the circles cases
		if (m_isCircle && other.m_isCircle) {
			return true;
		} else if (m_isCircle != other.m_isCircle) {
			return false;
		}
		
		// both not circles, time for fuzzy matching
		// rotate to a coord system where our offset is zero
		double point = mapZeroToTwoPi(other.m_offset - m_offset);
		return CompareReal.eq(m_length, other.m_length, epsilon) && (CompareReal.eq(point, 0.0, epsilon) || CompareReal.eq(point, TwoPi, epsilon));
	}
	
	@Override
	public String toString() {
		if (m_isCircle) {
			return "[Circle]";
		}
		return String.format("[%.1f,%.1f]", Math.toDegrees(getSource()), Math.toDegrees(getTarget()));
	}
	
	public List<Double> samplePoints(double stepSize) {
		int numSamples = Math.max(2, (int) (m_length / stepSize));
		return samplePoints(numSamples);
	}
	
	public List<Double> samplePoints(int numSamples) {
		List<Double> points = new ArrayList<Double>();
		for (int i = 0; i < numSamples; i++) {
			double t = m_offset + m_length * (double)i / (double) (numSamples - 1);
			points.add(mapMinusPiToPi(t));
		}
		assert (isEq(points.get(0), getSource()));
		assert (isEq(points.get(points.size() - 1), getTarget()));
		return points;
	}
	
	public List<CircleRange> split(double theta) {
		// PRECONDITION: theta is on the interior of the range
		if (!containsPointOnInterior(theta, 1e-6)) {
			throw new IllegalArgumentException("theta must be on the interior of the range!");
		}
		
		theta = mapMinusPiToPi(theta);
		List<CircleRange> ranges = new ArrayList<CircleRange>();
		ranges.add(CircleRange.newByCounterclockwiseSegment(getSource(), theta));
		ranges.add(CircleRange.newByCounterclockwiseSegment(theta, getTarget()));
		
		// DEBUG: check the lengths
		if (true) {
			double sum = 0.0;
			for (CircleRange range : ranges) {
				sum += range.m_length;
			}
			assert (CompareReal.eq(m_length, sum));
		}
		
		return ranges;
	}
	
	public void rotate(double rotationAngle) {
		m_offset += rotationAngle;
		m_offset = mapMinusPiToPi(m_offset);
	}
	
	public double getDistance(double theta) {
		if (m_isCircle) {
			return 0.0;
		}
		
		// rotate to a coord system where our offset is zero
		theta = mapZeroToTwoPi(mapZeroToTwoPi(theta) - mapZeroToTwoPi(m_offset));
		
		return Math.max(0, Math.min(theta - m_length, Math.PI * 2 - theta));
	}
	
	@Override
	public int hashCode() {
		return HashCalculator.combineHashes(Double.valueOf(m_offset).hashCode(), Double.valueOf(m_length).hashCode());
	}
	
	/*********************************
	 * Functions
	 *********************************/
	
	private void setCircle() {
		m_isCircle = true;
		m_offset = 0.0;
		m_length = TwoPi;
	}
}
