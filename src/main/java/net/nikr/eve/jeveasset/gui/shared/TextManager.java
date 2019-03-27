/*
 * Copyright 2009-2019 Contributors (see credits.txt)
 *
 * This file is part of jEveAssets.
 *
 * jEveAssets is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * jEveAssets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jEveAssets; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package net.nikr.eve.jeveasset.gui.shared;

import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import net.nikr.eve.jeveasset.gui.images.Images;
import net.nikr.eve.jeveasset.gui.shared.components.CompoundUndoManager;
import net.nikr.eve.jeveasset.i18n.GuiShared;


public final class TextManager {

	private enum CopyPopupAction {
		CUT, COPY, PASTE
	}

	private final JTextComponent component;
	private final JPopupMenu jPopupMenu;
	private final JMenuItem jCut;
	private final JMenuItem jCopy;
	private final JMenuItem jPaste;
	private final JMenuItem jUndo;
	private final JMenuItem jRedo;

	private final Clipboard clipboard;

	
	public static void installAll(final Container container) {
		for (Component component : container.getComponents()) {
			if (component instanceof Container) {
				installAll((Container) component);
			}
			if (component instanceof JTextComponent) {
				installTextComponent((JTextComponent) component);
			}
		}
	}

	public static void installTextComponent(final JTextComponent component) {
		//Make sure this component does not already have a UndoManager
		Document document = component.getDocument();
		boolean found = false;
		if (document instanceof AbstractDocument) {
			AbstractDocument abstractDocument = (AbstractDocument) document;
			for (UndoableEditListener editListener : abstractDocument.getUndoableEditListeners()) {
				if (editListener.getClass().equals(CompoundUndoManager.class)) {
					CompoundUndoManager undoManager = (CompoundUndoManager) editListener;
					undoManager.reset();
					return;
				}
			}
		}
		if (!found) {
			new TextManager(component);
		}
	}

	private TextManager(final JTextComponent component) {
		this.component = component;

		ListenerClass listener = new ListenerClass();

		component.addMouseListener(listener);

		jPopupMenu = new JPopupMenu();

		jCut = new JMenuItem(GuiShared.get().cut());
		jCut.setIcon(Images.EDIT_CUT.getIcon());
		jCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		jCut.setActionCommand(CopyPopupAction.CUT.name());
		jCut.addActionListener(listener);

		jCopy = new JMenuItem(GuiShared.get().copy());
		jCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		jCopy.setIcon(Images.EDIT_COPY.getIcon());
		jCopy.setActionCommand(CopyPopupAction.COPY.name());
		jCopy.addActionListener(listener);

		jPaste = new JMenuItem(GuiShared.get().paste());
		jPaste.setIcon(Images.EDIT_PASTE.getIcon());
		jPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		jPaste.setActionCommand(CopyPopupAction.PASTE.name());
		jPaste.addActionListener(listener);

		clipboard = component.getToolkit().getSystemClipboard();

		CompoundUndoManager undoManager = new CompoundUndoManager(component);

		jUndo = new JMenuItem(undoManager.getUndoAction());
		jUndo.setIcon(Images.EDIT_UNDO.getIcon());

		jRedo = new JMenuItem(undoManager.getRedoAction()); 
		jRedo.setIcon(Images.EDIT_REDO.getIcon());
	}

	private void showPopupMenu(final MouseEvent e) {
		if (!component.isFocusable()) { //Don't show anything for unfocusable components
			return;
		}

		if (!component.hasFocus()) {
			component.requestFocus();
		}

		jPopupMenu.removeAll();

		String s = component.getSelectedText();
		boolean canCopy = true;
		if (s == null) {
			canCopy = false;
		} else if (s.length() == 0) {
			canCopy = false;
		}

		if (component.isEditable()) {
			jCut.setEnabled(canCopy);
			jPopupMenu.add(jCut);
		}

		jCopy.setEnabled(canCopy);
		jPopupMenu.add(jCopy);

		if (component.isEditable()) {
			jPopupMenu.add(jPaste);
			jPopupMenu.addSeparator();
			jPopupMenu.add(jUndo);
			jPopupMenu.add(jRedo);
		}

		jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private class ListenerClass implements MouseListener, ActionListener {

		@Override
		public void mouseClicked(final MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopupMenu(e);
			}
		}

		@Override
		public void mousePressed(final MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopupMenu(e);
			}
		}

		@Override
		public void mouseReleased(final MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopupMenu(e);
			}
		}

		@Override
		public void mouseEntered(final MouseEvent e) { }

		@Override
		public void mouseExited(final MouseEvent e) { }

		@Override
		public void actionPerformed(final ActionEvent e) {
			if (CopyPopupAction.CUT.name().equals(e.getActionCommand())) {
				String s = component.getSelectedText();
				if (s == null) {
					return;
				}
				if (s.length() == 0) {
					return;
				}
				String text = component.getText();
				String before = text.substring(0, component.getSelectionStart());
				String after = text.substring(component.getSelectionEnd(), text.length());
				component.setText(before + after);
				StringSelection st = new StringSelection(s);
				clipboard.setContents(st, null);
			}
			if (CopyPopupAction.COPY.name().equals(e.getActionCommand())) {
				String s = component.getSelectedText();
				if (s == null) {
					return;
				}
				if (s.length() == 0) {
					return;
				}
				StringSelection st = new StringSelection(s);
				clipboard.setContents(st, null);
			}
			if (CopyPopupAction.PASTE.name().equals(e.getActionCommand())) {
				Transferable transferable = clipboard.getContents(this);
				try {
					String s = (String) transferable.getTransferData(DataFlavor.stringFlavor);
					String text = component.getText();
					String before = text.substring(0, component.getSelectionStart());
					String after = text.substring(component.getSelectionEnd(), text.length());
					component.setText(before + s + after);
					int caretPosition = before.length() + s.length();
					if (caretPosition <= component.getText().length()) {
						component.setCaretPosition(before.length() + s.length());
					}
				} catch (UnsupportedFlavorException ex) {

				} catch (IOException ex) {

				}
			}
		}

	}
}
