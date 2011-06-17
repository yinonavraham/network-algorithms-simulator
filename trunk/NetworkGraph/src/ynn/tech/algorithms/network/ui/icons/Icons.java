package ynn.tech.algorithms.network.ui.icons;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Icons
{
	private static URL getResource(String resource)
	{
		return Icons.class.getResource(resource);
	}
	
	private static Icon getIcon(String iconPath)
	{
		URL resource = getResource(iconPath);
		ImageIcon icon = new ImageIcon(resource);
		if (icon.getIconWidth() > 16 || icon.getIconHeight() > 16)
		{
			int width = Math.min(16, icon.getIconWidth());
			int height = Math.min(16, icon.getIconHeight());
			icon = new ImageIcon(getScaledImage(icon.getImage(), width, height));
		}
		return icon;
	}
	
	private static Image getScaledImage(Image srcImg, int w, int h) {
	    BufferedImage resizedImg = new BufferedImage(w, h,
	        BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	  }
	
	public static Icon getNew()
	{
		return getIcon("hot/New.png");
	}
	
	public static Icon getOpen()
	{
		return getIcon("hot/Open.png");
	}
	
	public static Icon getSave()
	{
		return getIcon("hot/Save.png");
	}
	
	public static Icon getEditMode()
	{
		return getIcon("hot/Design.png");
	}
	
	public static Icon getEditModeConnect()
	{
		return getIcon("Branch16.png");
	}
	
	public static Icon getEditModeMove()
	{
		return getIcon("Element_Copy16.png");
	}
	
	public static Icon getInsert()
	{
		return getIcon("Element_Add16.png");
	}
	
	public static Icon getDelete()
	{
		return getIcon("Element_Delete16.png");
	}
	
	public static Icon getPlay()
	{
		return getIcon("Play.png");
	}
	
	public static Icon getStop()
	{
		return getIcon("Stop.png");
	}
	
	public static Icon getRewind()
	{
		return getIcon("Rewind.png");
	}
	
	public static Icon getForward()
	{
		return getIcon("Forward.png");
	}
	
	public static Icon getAbout()
	{
		return getIcon("About.png");
	}
	
	public static Icon getClipboard()
	{
		return getIcon("Clipboard.png");
	}
	
	public static Icon getConsole()
	{
		return getIcon("Console.png");
	}
	
	public static Icon getStep()
	{
		return getIcon("Step16.png");
	}
	
	public static Icon getUndo()
	{
		return getIcon("hot/Undo.png");
	}
	
	public static Icon getAlgorithm()
	{
		return getIcon("Scroll.png");
	}
	
	public static Image getNetwork()
	{
		return new ImageIcon(getResource("network.png")).getImage();
	}
}
