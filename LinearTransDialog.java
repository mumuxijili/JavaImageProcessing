import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LinearTransDialog extends JPanel{
	private JLabel label1 = new JLabel("斜率");
	private JLabel label2 = new JLabel("截距");
	private JTextField tField1 = new JTextField("1.5");
	private JTextField tField2 = new JTextField("10");
	private JButton button1 = new JButton("Confirm");
	private JButton button2 = new JButton("Cancel");
	private JDialog dialog;
	private boolean ok;
	private double a, b;
	private double [] r = new double [2];
	
	public LinearTransDialog()
	{
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		panel.add(label1);
		panel.add(tField1);
		panel.add(label2);
		panel.add(tField2);
		add(panel, BorderLayout.CENTER);
		
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
	
	public void setPara(String a, String b)
	{
		tField1.setText(a);
		tField2.setText(b);
	}
	
	public double[] getPara()
	{
		a = Double.parseDouble(tField1.getText());
		b = Double.parseDouble(tField2.getText());
		//System.out.println("a =" + a + "end");
		r[0] = a;
		r[1] = b;
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
