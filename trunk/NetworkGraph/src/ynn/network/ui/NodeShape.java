package ynn.network.ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class NodeShape extends AbstractShape
{
	Ellipse2D.Double _ellipse;
	Rectangle2D.Double _rect;

	public NodeShape()
	{
		super();
		_ellipse = new Ellipse2D.Double();
		_ellipse.x = 0;
		_ellipse.y = 0;
		_ellipse.width = 50;
		_ellipse.height = 50;
		_rect = new Rectangle2D.Double();
		_rect.width = 20;
		_rect.height = 20;
		updateAnnotationPosition();
	}
	
	private void updateAnnotationPosition()
	{
		_rect.x = _ellipse.x + _ellipse.width - 10;
		_rect.y = _ellipse.y + (10 - _rect.height);
	}

	@Override
	protected void drawAnnotationLine(Graphics2D g)
	{
		updateAnnotationRectSize(g);
		g.draw(_rect);
	}

	@Override
	protected void drawAnnotationFill(Graphics2D g)
	{
		updateAnnotationRectSize(g);
		g.fill(_rect);
	}

	@Override
	protected void drawAnnotationText(Graphics2D g)
	{
		updateAnnotationRectSize(g);
		String text = getAnnotationText();
		FontMetrics fontMetrics = g.getFontMetrics(getAnnotationFont());
        float textX = (float) (_rect.x + (_rect.width - fontMetrics.stringWidth(text)) / 2);
        float textY = (float) (_rect.y + (_rect.height + fontMetrics.getHeight() / 2) / 2);
        g.drawString(text, textX, textY);
	}
	
	private void updateAnnotationRectSize(Graphics2D g)
	{
		String text = getAnnotationText();
		FontMetrics fontMetrics = g.getFontMetrics(getAnnotationFont());
        _rect.width = fontMetrics.stringWidth(text) + 4;
        _rect.height = fontMetrics.getHeight() + 4;
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
		updateAnnotationPosition();
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
		updateAnnotationPosition();
		fireShapeMoved(old, pos);
	}
	
	public Point2D getPosition()
	{
		return new Point2D.Double(_ellipse.x,_ellipse.y);
	}
	
	@Override
	public String toString()
	{
		return String.format("(%s)", getText());
	}

	@Override
	public Dimension getDimension()
	{
		int width = (int)_ellipse.width;
		int height = (int)_ellipse.height;
		return new Dimension(width, height);
	}
}
