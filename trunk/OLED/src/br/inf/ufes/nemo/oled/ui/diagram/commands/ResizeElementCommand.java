/**
 * Copyright 2007 Wei-ju Wu
 *
 * This file is part of TinyUML.
 *
 * TinyUML is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * TinyUML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TinyUML; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package br.inf.ufes.nemo.oled.ui.diagram.commands;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import javax.swing.undo.AbstractUndoableEdit;


import br.inf.ufes.nemo.oled.draw.DoubleDimension;
import br.inf.ufes.nemo.oled.draw.Node;
import br.inf.ufes.nemo.oled.util.Command;


/**
 * This class implements a resizing command.
 *
 * @author Wei-ju Wu, Antognoni Albuquerque
 * @version 1.1
 */
public class ResizeElementCommand extends AbstractUndoableEdit
implements Command {

	private static final long serialVersionUID = -3090945928366890788L;
	private DiagramEditorNotification notification;
	private Node element;
	private Point2D newpos = new Point2D.Double(), oldpos = new Point2D.Double();
	private Dimension2D newsize = new DoubleDimension(), oldsize = new DoubleDimension();

	/**
	 * Constructor.
	 * @param aNotification the DiagramEditorNotification object
	 * @param anElement the element to resize
	 * @param aNewPos the new position
	 * @param aNewSize the new size
	 */
	public ResizeElementCommand(DiagramEditorNotification aNotification, Node anElement, Point2D aNewPos, Dimension2D aNewSize) {
		notification = aNotification;
		element = anElement;
		newpos.setLocation(aNewPos);
		newsize.setSize(aNewSize);
		oldpos.setLocation(element.getAbsoluteX1(), element.getAbsoluteY1());
		oldsize.setSize(element.getSize().getWidth(), element.getSize().getHeight());
	}

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		element.setAbsolutePos(newpos.getX(), newpos.getY());
		element.setSize(newsize.getWidth(), newsize.getHeight());
		notification.notifyElementResized(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void redo() {
		super.redo();
		run();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void undo() {
		super.undo();
		element.setAbsolutePos(oldpos.getX(), oldpos.getY());
		element.setSize(oldsize.getWidth(), oldsize.getHeight());
		notification.notifyElementResized(element);
	}
}
