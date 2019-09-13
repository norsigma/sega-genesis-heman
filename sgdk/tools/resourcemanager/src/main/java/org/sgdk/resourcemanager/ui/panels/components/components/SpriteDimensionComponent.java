package org.sgdk.resourcemanager.ui.panels.components.components;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.sgdk.resourcemanager.entities.SGDKSprite;

public class SpriteDimensionComponent extends JPanel{
		
	private static final long serialVersionUID = 1L;
	private SGDKSprite sprite = null;
	private JTextField w = new JTextField();
	private JTextField h = new JTextField();

	private static final int SCALE_MULTIPLICATOR = 8;
	
	public SpriteDimensionComponent() {
		super(new GridLayout(1,4));
		setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"Sprite Dimensions",
				TitledBorder.RIGHT,
				TitledBorder.ABOVE_TOP
			)
		);
		
		w.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sprite != null){
					if(Float.valueOf(w.getText()).intValue() % SCALE_MULTIPLICATOR == 0) {						
						sprite.setWidth(Math.round(Float.valueOf(w.getText()) / SCALE_MULTIPLICATOR));
					}
				}
			}
		});
		
		h.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sprite != null){
					if(Float.valueOf(w.getText()).intValue() % SCALE_MULTIPLICATOR == 0) {	
						sprite.setHeigth(Math.round(Float.valueOf(h.getText()) / SCALE_MULTIPLICATOR));
					}
				}
			}
		});
		add(new JLabel("w:"));
		add(w);
		add(new JLabel("h:"));
		add(h);
	}
	
	public void setSGDKSprite(SGDKSprite sprite) {
		this.sprite = sprite;
		w.setText(""+ (sprite.getWidth() * SCALE_MULTIPLICATOR));
		h.setText(""+ (sprite.getHeigth() * SCALE_MULTIPLICATOR));
	}
	
}
