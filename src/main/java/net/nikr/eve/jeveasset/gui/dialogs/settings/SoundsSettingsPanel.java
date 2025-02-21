/*
 * Copyright 2009-2023 Contributors (see credits.txt)
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
package net.nikr.eve.jeveasset.gui.dialogs.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import net.nikr.eve.jeveasset.Program;
import net.nikr.eve.jeveasset.data.settings.Settings;
import net.nikr.eve.jeveasset.gui.images.Images;
import net.nikr.eve.jeveasset.i18n.DialoguesSettings;
import net.nikr.eve.jeveasset.gui.sounds.SoundPlayer;
import net.nikr.eve.jeveasset.gui.sounds.Sounds;


public class SoundsSettingsPanel extends JSettingsPanel {

	public static enum SoundsOption {
		OUTBID_UPDATE_COMPLETED() {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsOutbidUpdateCompleted();
			}
		},
		INDUSTRY_JOB_COMPLETED() {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsIndustryJobCompleted();
			}
		};

		@Override
		public String toString() {
			return getText();
		}

		public abstract String getText();
	}

	

	public static enum SoundsSound {
		NONE(null) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsNone();
			}
		},
		ARMOR(Sounds.ARMOR) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsEveArmor();
			}
		},
		CAPACITOR(Sounds.CAPACITOR) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsEveCapacitor();
			}
		},
		CARGO(Sounds.CARGO) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsEveCargo();
			}
		},
		CHARACTER_SELECT(Sounds.CHARACTER_SELECT) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsEveCharacterSelection();
			}
		},
		LOGIN(Sounds.LOGIN) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsEveLogin();
			}
		},
		NOTIFICATION_PING(Sounds.NOTIFICATION_PING) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsEveNotificationPing();
			}
		},
		SHIELD(Sounds.SHIELD) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsEveShield();
			}
		},
		SKILL(Sounds.SKILL) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsEveSkill();
			}
		},
		START(Sounds.START) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsEveStart();
			}
		},
		STRUCTURE(Sounds.STRUCTURE) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsEveStructure();
			}
		},
		BEEP(null) {
			@Override
			public String getText() {
				return DialoguesSettings.get().soundsBeep();
			}
		};

		private final Sounds sounds;

		private SoundsSound(Sounds sounds) {
			this.sounds = sounds;
		}

		public Sounds getSound() {
			return sounds;
		}

		@Override
		public String toString() {
			return getText();
		}

		public abstract String getText();
	}

	private final List<SoundPanel> soundPanels = new ArrayList<>();

	public SoundsSettingsPanel(final Program program, final SettingsDialog settingsDialog) {
		super(program, settingsDialog, DialoguesSettings.get().sounds(), Images.MISC_SOUNDS.getIcon());

		soundPanels.add(new SoundPanel(SoundsOption.OUTBID_UPDATE_COMPLETED, SoundsSound.values()));
		soundPanels.add(new SoundPanel(SoundsOption.INDUSTRY_JOB_COMPLETED, SoundsSound.values()));

		GroupLayout.ParallelGroup horizontalLabels = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		GroupLayout.ParallelGroup horizontalComboBoxs = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		GroupLayout.ParallelGroup horizontalPlays = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		GroupLayout.ParallelGroup horizontalStops = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();
		for (SoundPanel panel : soundPanels) {
			horizontalLabels.addComponent(panel.jLabel);
			horizontalComboBoxs.addComponent(panel.jComboBox);
			horizontalPlays.addComponent(panel.jPlay, Program.getIconButtonsWidth(), Program.getIconButtonsWidth(), Program.getIconButtonsWidth());
			horizontalStops.addComponent(panel.jStop, Program.getIconButtonsWidth(), Program.getIconButtonsWidth(), Program.getIconButtonsWidth());
			verticalGroup.addGroup(
				layout.createParallelGroup()
					.addComponent(panel.jLabel, Program.getButtonsHeight(), Program.getButtonsHeight(), Program.getButtonsHeight())
					.addComponent(panel.jComboBox, Program.getButtonsHeight(), Program.getButtonsHeight(), Program.getButtonsHeight())
					.addComponent(panel.jPlay, Program.getButtonsHeight(), Program.getButtonsHeight(), Program.getButtonsHeight())
					.addComponent(panel.jStop, Program.getButtonsHeight(), Program.getButtonsHeight(), Program.getButtonsHeight())
			);
		}
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(horizontalLabels)
				.addGroup(horizontalComboBoxs)
				.addGroup(horizontalPlays)
				.addGroup(horizontalStops)
		
		);
		layout.setVerticalGroup(verticalGroup);
	}

	@Override
	public UpdateType save() {
		for (SoundPanel panel : soundPanels) {
			panel.save();
		}
		stopAll();
		return UpdateType.NONE;
	}

	@Override
	public void load() {
		for (SoundPanel panel : soundPanels) {
			panel.load();
		}
	}

	private void stopAll() {
		for (SoundPanel panel : soundPanels) {
			panel.stop();
		}
	}

	private class SoundPanel {
		private final SoundsOption option;
		private final JLabel jLabel;
		private final JComboBox<SoundsSound> jComboBox;
		private final JButton jPlay;
		private final JButton jStop;
		private SoundsSound playing = null;

		public SoundPanel(SoundsOption key, SoundsSound[] options) {
			this.option = key;
			jLabel = new JLabel(key.getText());
			jComboBox = new JComboBox<>(options);
			jPlay = new JButton(Images.MISC_PLAY.getIcon());
			jPlay.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					play();
				}
			});
			jStop = new JButton(Images.MISC_STOP.getIcon());
			jStop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					stopAll();
				}
			});
		}

		public void save() {
			SoundsSound sound = jComboBox.getItemAt(jComboBox.getSelectedIndex());
			Settings.get().getSoundSettings().put(option, sound);
		}

		public void load() {
			SoundsSound sound = Settings.get().getSoundSettings().get(option);
			if (sound == null) {
				jComboBox.setSelectedIndex(0);
			} else {
				jComboBox.setSelectedItem(sound);
			}
		}

		private void play() {
			stopAll();
			playing = jComboBox.getItemAt(jComboBox.getSelectedIndex());
			SoundPlayer.play(playing);
		}

		private void stop() {
			SoundPlayer.stop(playing);
		}
	}
}
