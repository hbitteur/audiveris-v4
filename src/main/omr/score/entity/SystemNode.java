//----------------------------------------------------------------------------//
//                                                                            //
//                            S y s t e m N o d e                             //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur 2000-2012. All rights reserved.                 //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.score.entity;

import omr.glyph.facets.Glyph;

import omr.score.common.PixelDimension;
import omr.score.common.PixelPoint;
import omr.score.common.PixelRectangle;
import omr.score.visitor.ScoreVisitor;

import omr.util.Navigable;
import omr.util.TreeNode;

import java.util.Collection;
import java.util.Collections;

/**
 * Class {@code SystemNode} is an abstract class that is subclassed
 * for any Node that is contained in a system, beginning by the system
 * itself.
 * So this class encapsulates a direct link to the enclosing system.
 *
 * @author Hervé Bitteur
 */
public abstract class SystemNode
        extends PageNode
{
    //~ Enumerations -----------------------------------------------------------

    /** Relative vertical position with respect to the staves of the system
     * or part at hand */
    public enum StaffPosition
    {
        //~ Enumeration constant initializers ----------------------------------

        /** Above the first real staff of this entity */
        ABOVE_STAVES,
        /** Somewhere
         * within the staves of this entity (system or part) */
        WITHIN_STAVES,
        /** Below the last staff of
         * this entity */
        BELOW_STAVES;

    }

    //~ Instance fields --------------------------------------------------------
    /** Containing system */
    @Navigable(false)
    private final ScoreSystem system;

    /** Bounding box of this entity, WRT system top-left corner */
    private PixelRectangle box;

    /** Location of the center of this entity, WRT system top-left corner */
    protected PixelPoint center;

    //~ Constructors -----------------------------------------------------------
    //------------//
    // SystemNode //
    //------------//
    /**
     * Create a SystemNode
     *
     * @param container the (direct) container of the node
     */
    public SystemNode (PageNode container)
    {
        super(container);

        // Set the system link
        for (TreeNode c = this; c != null; c = c.getParent()) {
            if (c instanceof ScoreSystem) {
                system = (ScoreSystem) c;

                return;
            }
        }

        system = null;
    }

    //~ Methods ----------------------------------------------------------------
    //--------//
    // accept //
    //--------//
    @Override
    public boolean accept (ScoreVisitor visitor)
    {
        return visitor.visit(this);
    }

    //----------//
    // addError //
    //----------//
    /**
     * Register a system-based error in the ErrorsWindow
     *
     * @param text the error message
     */
    public void addError (String text)
    {
        addError(null, text);
    }

    //----------//
    // addError //
    //----------//
    /**
     * Register a system-based error in the ErrorsWindow, with the
     * related glyph
     *
     * @param glyph the related glyph
     * @param text  the error message
     */
    public void addError (Glyph glyph,
                          String text)
    {
        if ((getPage() != null) && (getPage()
                .getSheet() != null)) {
            getPage()
                    .getSheet()
                    .addError(this, glyph, text);
        }
    }

    //--------------------//
    // computeGlyphCenter //
    //--------------------//
    /**
     * Compute the bounding center of a glyph
     *
     * @param glyph the glyph
     * @return the glyph center
     */
    public PixelPoint computeGlyphCenter (Glyph glyph)
    {
        return computeGlyphsCenter(Collections.singleton(glyph));
    }

    //--------//
    // getBox //
    //--------//
    /**
     * Report a copy of the bounding box of the entity.
     *
     * @return the box
     */
    public PixelRectangle getBox ()
    {
        if (box == null) {
            computeBox();
        }

        if (box != null) {
            return (PixelRectangle) box.clone();
        } else {
            return null;
        }
    }

    //-----------//
    // getCenter //
    //-----------//
    /**
     * Report a copy of the center of this entity.
     *
     * @return the center point
     */
    public PixelPoint getCenter ()
    {
        if (center == null) {
            computeCenter();
        }

        if (center != null) {
            return (PixelPoint) center.clone();
        } else {
            return null;
        }
    }

    //------------------//
    // getContextString //
    //------------------//
    @Override
    public String getContextString ()
    {
        StringBuilder sb = new StringBuilder(super.getContextString());
        sb.append("S")
                .append(system.getId());

        return sb.toString();
    }

    //--------------//
    // getDimension //
    //--------------//
    /**
     * Report the dimension of the entity
     *
     * @return the entity dimension
     */
    public PixelDimension getDimension ()
    {
        if (box == null) {
            computeBox();
        }

        if (box != null) {
            return new PixelDimension(box.width, box.height);
        } else {
            return null;
        }
    }

    //-----------//
    // getSystem //
    //-----------//
    /**
     * Report the containing system
     *
     * @return the containing system
     */
    public ScoreSystem getSystem ()
    {
        return system;
    }

    //------------//
    // computeBox //
    //------------//
    /**
     * Compute the bounding box of this entity.
     * Unless overridden, this method raises an exception.
     */
    protected void computeBox ()
    {
        throw new RuntimeException(
                "computeBox() not implemented in " + getClass().getName());
    }

    //---------------//
    // computeCenter //
    //---------------//
    /**
     * Compute the center of this entity.
     */
    protected void computeCenter ()
    {
        PixelRectangle theBox = getBox();

        if (theBox != null) {
            setCenter(theBox.getCenter());
        } else {
            setCenter(null);
        }
    }

    //------------------//
    // computeGlyphsBox //
    //------------------//
    /**
     * Compute the bounding box of a collection of glyphs
     *
     * @param glyphs the collection of glyph components
     * @return the bounding box
     */
    protected PixelRectangle computeGlyphsBox (
            Collection<? extends Glyph> glyphs)
    {
        if ((glyphs == null) || (getSystem() == null)) {
            return null;
        }

        PixelRectangle pixRect = null;

        for (Glyph glyph : glyphs) {
            if (pixRect == null) {
                pixRect = glyph.getBounds();
            } else {
                pixRect = pixRect.union(glyph.getBounds());
            }
        }

        return pixRect;
    }

    //---------------------//
    // computeGlyphsCenter //
    //---------------------//
    /**
     * Compute the bounding center of a collection of glyphs
     *
     * @param glyphs the collection of glyph components
     * @return the area center
     */
    protected PixelPoint computeGlyphsCenter (Collection<? extends Glyph> glyphs)
    {
        if (glyphs == null) {
            return null;
        }

        PixelRectangle glyphsBox = computeGlyphsBox(glyphs);

        if (glyphsBox == null) {
            return null;
        }
        
        return new PixelPoint(
                glyphsBox.x + (glyphsBox.width / 2),
                glyphsBox.y + (glyphsBox.height / 2));
    }

    //-------//
    // reset //
    //-------//
    /**
     * Invalidate cached data
     */
    protected void reset ()
    {
        box = null;
        center = null;
    }

    //--------//
    // setBox //
    //--------//
    /**
     * Assign the bounding box
     *
     * @param box the bounding box
     */
    protected void setBox (PixelRectangle box)
    {
        this.box = box;
    }

    //-----------//
    // setCenter //
    //-----------//
    /**
     * Remember the center of this system node
     *
     * @param center the system-based center of the system node
     */
    protected void setCenter (PixelPoint center)
    {
        this.center = center;
    }
}
