//----------------------------------------------------------------------------//
//                                                                            //
//                        G l y p h A l i g n m e n t                         //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//
// Copyright © Hervé Bitteur and others 2000-2017. All rights reserved.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//----------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.glyph.facets;

import org.audiveris.omr.math.Line;

import org.audiveris.omr.run.Orientation;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * Interface {@code GlyphAlignment} describes glyph alignment.
 * The key feature is the approximating Line on all points of the glyph.
 * The line can be the least-square fitted line, or a natural spline for more
 * complex cases.
 *
 * <ul>
 * <li>Staff lines, ledgers, alternate ends are examples of rather
 * horizontal glyphs.</li>
 * <li>Bar lines, stems are examples of rather vertical glyphs.</li>
 * <li>Other glyphs have no dominant orientation.</li>
 * </ul>
 *
 * <p>Note that a glyph has no predefined orientation, only the slope of its
 * approximating line is relevant and allows to disambiguate between the
 * start point and the stop point. If abs(tangent) is less than 45 degrees we
 * have a rather horizontal glyph, otherwise a rather vertical glyph.</p>
 *
 * @author Hervé Bitteur
 */
public interface GlyphAlignment
        extends GlyphFacet
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Report the average thickness, using the provided orientation.
     *
     * @return the average thickness
     */
    public double getMeanThickness (Orientation orientation);

    /**
     * Report the ratio of length over thickness, using provided
     * orientation.
     *
     * @param orientation the general orientation reference
     * @return the "slimness" of the glyph
     * @see #getLength
     * @see #getThickness
     */
    double getAspect (Orientation orientation);

    /**
     * Compute the number of pixels stuck on first side of the glyph.
     *
     * @return the number of pixels
     */
    int getFirstStuck ();

    /**
     * Report the co-tangent of glyph line angle with abscissa axis
     *
     * @return co-tangent of heading angle (dx/dy).
     */
    double getInvertedSlope ();

    /**
     * Compute the nb of pixels stuck on last side of the glyph.
     *
     * @return the number of pixels
     */
    int getLastStuck ();

    /**
     * Report the length of the glyph, along the provided orientation.
     *
     * @param orientation the general orientation reference
     * @return the glyph length in pixels
     */
    int getLength (Orientation orientation);

    /**
     * Return the approximating line computed on the glyph, as an
     * <b>absolute</b> line, with x for horizontal axis and y for
     * vertical axis.
     *
     * @return The absolute line
     */
    Line getLine ();

    /**
     * Return the mean quadratic distance of the defining population
     * of points to the resulting line.
     * This can be used to measure how well the line fits the points.
     *
     * @return the absolute value of the mean distance
     */
    double getMeanDistance ();

    /**
     * Return the position at the middle of the glyph,
     * using the provided orientation.
     *
     * @return the position of the middle of the glyph
     */
    int getMidPos (Orientation orientation);

    /**
     * Report the precise glyph position for the provided coordinate.
     *
     * @param coord       the coord value (x for horizontal, y for vertical)
     * @param orientation the general orientation reference
     * @return the pos value (y for horizontal, x for vertical)
     */
    double getPositionAt (double coord,
                          Orientation orientation);

    /**
     * Report the absolute centroid of all the glyph pixels found in the
     * provided absolute ROI
     *
     * @param absRoi the desired absolute region of interest
     * @return the absolute barycenter of the pixels found
     */
    Point2D getRectangleCentroid (Rectangle absRoi);

    /**
     * Report the tangent of glyph line angle with abscissa axis
     *
     * @return tangent of heading angle (dy/dx).
     */
    double getSlope ();

    /**
     * Report the absolute point at the beginning (with respect to the provided
     * orientation) of the approximating line.
     *
     * @param orientation the general orientation reference
     * @return the starting point of the glyph line
     */
    Point2D getStartPoint (Orientation orientation);

    /**
     * Report the absolute point at the end (with respect to the provided
     * orientation) of the approximating line.
     *
     * @param orientation the general orientation reference
     * @return the ending point of the line
     */
    Point2D getStopPoint (Orientation orientation);

    /**
     * Report the glyph thickness across the desired orientation.
     *
     * @param orientation the general orientation reference
     * @return the thickness in pixels
     */
    int getThickness (Orientation orientation);

    /**
     * Report the resulting thickness of this glyph at the provided
     * coordinate, using a predefined probe width.
     *
     * @param coord       the desired abscissa
     * @param orientation the general orientation reference
     * @return the thickness measured, expressed in number of pixels.
     */
    double getThicknessAt (double coord,
                           Orientation orientation);

    /**
     * Render the main guiding line of the glyph, using the current
     * foreground color.
     *
     * @param g the graphic context
     */
    void renderLine (Graphics2D g);

    /**
     * Force the locations of start point and stop points.
     *
     * @param pStart new start point
     * @param pStop  new stop point
     */
    void setEndingPoints (Point2D pStart,
                          Point2D pStop);
}
