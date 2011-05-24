package ynn.network.ui;

import java.awt.geom.Point2D;

public class ShapeMovedEvent
{
	private Point2D _oldPoint;
	private Point2D _newPoint;
	private Point2D _deltaPoint;
	private AbstractShape _shape;
	
	public ShapeMovedEvent(AbstractShape shape, Point2D oldPoint, Point2D newPoint)
	{
		_shape = shape;
		_oldPoint = oldPoint;
		_newPoint = newPoint;
		_deltaPoint = new Point2D.Double(newPoint.getX() - oldPoint.getX(), newPoint.getY() - oldPoint.getY());
	}
	
	public ShapeMovedEvent(AbstractShape shape, Point2D deltaPoint)
	{
		_shape = shape;
		_oldPoint = null;
		_newPoint = null;
		_deltaPoint = deltaPoint;
	}
	
	public AbstractShape getShape()
	{
		return _shape;
	}
	
	public Point2D getOldPoint()
	{
		return _oldPoint;
	}
	
	public Point2D getNewPoint()
	{
		return _newPoint;
	}
	
	public Point2D getDeltaPoint()
	{
		return _deltaPoint;
	}
}
