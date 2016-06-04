import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GrayStretchDialog extends JPanel{
	private JLabel label1 = new JLabel("原值1");
	private JLabel label2 = new JLabel("目标值1");
	private JLabel label3 = new JLabel("原值2");
	private JLabel label4 = new JLabel("目标值2");
	private JLabel label5 = new JLabel("请务必保证原值1<原值2, 目标值1<目标值2且均小于255");
	private JTextField tField1 = new JTextField("40");
	private JTextField tField2 = new JTextField("60");
	private JTextField tField3 = new JTextField("200");
	private JTextField tField4 = new JTextField("220");
	private JButton button1 = new JButton("Confirm");
	private JButton button2 = new JButton("Cancel");
	private JDialog dialog;
	private boolean ok;
	private int r1, s1, r2, s2;
	private int [] r = new int [4];
	
	public GrayStretchDialog()
	{
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2));
		panel.add(label1);
		panel.add(tField1);
		panel.add(label2);
		panel.add(tField2);
		panel.add(label3);
		panel.add(tField3);
		panel.add(label4);
		panel.add(tField4);
		add(panel, BorderLayout.NORTH);
		add(label5, BorderLayout.CENTER);
		
		button1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				ok = true;
				dialog.setVisible(false);
			}
		});
		
		button2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				dialog.setVisible(false);
				
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		add(buttonPanel,BorderLayout.SOUTH);
	}
	
	public void setPara(String a, String b, String c, String d)
	{
		tField1.setText(a);
		tField2.setText(b);
		tField3.setText(c);
		tField4.setText(d);
	}
	
	public int[] getPara()
	{
		r1 = Integer.parseInt(tField1.getText());
		s1 = Integer.parseInt(tField2.getText());
		r2 = Integer.parseInt(tField3.getText());
		s2 = Integer.parseInt(tField4.getText());
		r[0] = r1;
		r[1] = s1;
		r[2] = r2;
		r[3] = s2;
		return r;
	}
	
	public boolean showDialog(Component parent, String title)
	{
		ok = false;
		
		Frame owner = null;
		if(parent instanceof Frame)
			owner = (Frame)parent;
		else
			owner = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);
		
		if(dialog == null || dialog.getOwner() != owner)
		{
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(button1);
			dialog.pack();
		}
		
		dialog.setTitle(title);
		dialog.setVisible(true);
		return ok;
	}
}
