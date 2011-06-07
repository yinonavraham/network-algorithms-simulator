package ynn.tech.algorithms.network.ui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.invokeAndWait(new Runnable()
			{
				@Override
				public void run()
				{
					MainWindow window = new MainWindow();
					window.setVisible(true);
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
