//----------------------------------------------------------------------------//
//                                                                            //
//                           B a r l i n e T a s k                            //
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
package org.audiveris.omr.script;

import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.grid.GridBuilder;

import org.audiveris.omr.sheet.Sheet;

import org.audiveris.omr.step.Stepping;
import org.audiveris.omr.step.Steps;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Class {@code BarlineTask} assigns (or deassigns) a barline shape to
 * a collection of glyphs.
 *
 * <p>If the compound flag is set, a compound glyph is composed from the
 * provided glyphs and assigned the shape. Otherwise, each provided glyph is
 * assigned the shape.</p>
 *
 * @author Hervé Bitteur
 */
public class BarlineTask
        extends AssignTask
{
    //~ Constructors -----------------------------------------------------------

    //-------------//
    // BarlineTask //
    //-------------//
    /**
     * Create a barline assignment task.
     *
     * @param sheet    the containing sheet
     * @param shape    the assigned shape (or null for a de-assignment)
     * @param compound true if all glyphs are to be merged into one compound
     *                 which is assigned to the given shape, false if each and
     *                 every glyph is to be assigned to the given shape
     * @param glyphs   the collection of concerned glyphs
     */
    public BarlineTask (Sheet sheet,
                        Shape shape,
                        boolean compound,
                        Collection<Glyph> glyphs)
    {
        super(sheet, shape, compound, glyphs);
    }

    //-------------//
    // BarlineTask //
    //-------------//
    /**
     * Convenient way to create a barline deassignment task.
     *
     * @param glyphs the collection of glyphs to deassign
     */
    public BarlineTask (Sheet sheet,
                        Collection<Glyph> glyphs)
    {
        super(sheet, glyphs);
    }

    //-------------//
    // BarlineTask //
    //-------------//
    /** No-arg constructor for JAXB only */
    protected BarlineTask ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //
    //--------//
    // epilog //
    //--------//
    /**
     * The amount or work depends on what is impacted by the barline(s)
     * assignment or deassignment.
     * <p>If the modifications do not concern system-defining barlines but only
     * measure or parts, the systems limits are not impacted.
     * Otherwise, the whole systems structure has to be redone ...!!!!
     *
     * @param sheet the impacted sheet
     */
    @Override
    public void epilog (Sheet sheet)
    {
        try {
            GridBuilder gridBuilder = sheet.getGridBuilder();

            if (getAssignedShape() != null) {
                // Assignment
                if (isCompound()) {
                    Glyph firstGlyph = glyphs.iterator()
                            .next();
                    Glyph compound = firstGlyph.getMembers()
                            .first()
                            .getGlyph();
                    gridBuilder.updateBars(glyphs, Arrays.asList(compound));
                } else {
                    gridBuilder.updateBars(glyphs, glyphs);
                }
            } else {
                // Deassignment
                Set<Glyph> emptySet = Collections.emptySet();
                gridBuilder.updateBars(glyphs, emptySet);
            }

            // Following steps
            Stepping.reprocessSheet(
                    Steps.valueOf(Steps.SYSTEMS),
                    sheet,
                    sheet.getSystems(),
                    false);
        } catch (Exception ex) {
            logger.warn("Error in BarlineTask", ex);
        }
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder(super.internalsString());
        sb.append(" barline");

        return sb.toString();
    }
}
