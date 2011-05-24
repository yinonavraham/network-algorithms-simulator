package ynn.network.ui;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class NodeShape extends AbstractShape
{
	Ellipse2D.Double _ellipse;

	public NodeShape()
	{
		super();
		_ellipse = new Ellipse2D.Double();
		_ellipse.x = 0;
		_ellipse.y = 0;
		_ellipse.width = 50;
		_ellipse.height = 50;
	}

	@Override
	protected void drawShapeFill(Graphics2D g)
	{
		g.fill(_ellipse);
	}

	@Override
	protected void drawShapeLine(Graphics2D g)
	{
		g.draw(_ellipse);
	}

	@Override
	protected void drawShapeEmphasis(Graphics2D g)
	{
		g.setStroke(new BasicStroke(getLineWidth() + 3));
		g.draw(_ellipse);
	}
	
	@Override
	protected void drawShapeText(Graphics2D g)
	{
        FontMetrics fontMetrics = g.getFontMetrics(getFont());
        float textX = (float) (_ellipse.x + (_ellipse.width - fontMetrics.stringWidth(getText())) / 2);
        float textY = (float) (_ellipse.y + (_ellipse.height + fontMetrics.getHeight() / 2) / 2);
        g.drawString(getText(), textX, textY);
	}

	@Override
	protected void doMove(double dx, double dy)
	{
		_ellipse.x += dx;
		_ellipse.y += dy;
	}

	@Override
	public boolean contains(double x, double y)
	{
		return _ellipse.contains(x, y);
	}

	@Override
	public Point2D getCenter()
	{
		return new Point2D.Double(_ellipse.getCenterX(),_ellipse.getCenterY());
	}
	
	public void setPosition(Point2D pos)
	{
		Point2D old = getPosition();
		_ellipse.x = pos.getX();
		_ellipse.y = pos.getY();
		fireShapeMoved(old, pos);
	}
	
	public Point2D getPosition()
	{
		return new Point2D.Double(_ellipse.x,_ellipse.y);
	}

}
