package ynn.network.ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class ConnectorShape extends AbstractShape implements IShapeListener
{
	public enum Direction { None, Default, Other, Both }
	
	private AbstractShape _vertex1 = null;
	private AbstractShape _vertex2 = null;
	private Line2D.Double _line;
	private Direction _direction = Direction.None;
	
	public ConnectorShape()
	{
		super();
		_line = new Line2D.Double();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		_vertex1.removeShapeListener(this);
		_vertex2.removeShapeListener(this);
		_vertex1 = null;
		_vertex2 = null;
	}
	
	public void setVertex1(AbstractShape shape)
	{
		if (_vertex1 != null) _vertex1.removeShapeListener(this);
		_vertex1 = shape;
		if (shape != null)
		{
			_line.x1 = shape.getCenter().getX();
			_line.y1 = shape.getCenter().getY();
			_vertex1.addShapeListener(this);
		}
	}
	
	public AbstractShape getVertex1()
	{
		return _vertex1;
	}
	
	public void setVertex2(AbstractShape shape)
	{
		if (_vertex2 != null) _vertex2.removeShapeListener(this);
		_vertex2 = shape;
		if (shape != null)
		{
			_line.x2 = shape.getCenter().getX();
			_line.y2 = shape.getCenter().getY();
			_vertex2.addShapeListener(this);
		}
	}
	
	public AbstractShape getVertex2()
	{
		return _vertex2;
	}
	
	public void setPoint1(double x, double y)
	{
		_line.x1 = x;
		_line.y1 = y;
	}
	
	public void setPoint2(double x, double y)
	{
		_line.x2 = x;
		_line.y2 = y;
	}
	
	public void setDirection(Direction direction)
	{
		_direction = direction == null ? Direction.None : direction;
	}
	
	public Direction getDirection()
	{
		return _direction;
	}

	@Override
	protected void drawAnnotationFill(Graphics2D g)
	{
	}

	@Override
	protected void drawAnnotationLine(Graphics2D g)
	{
	}

	@Override
	protected void drawAnnotationText(Graphics2D g)
	{
	}

	@Override
	protected void drawShapeFill(Graphics2D g)
	{
	}

	@Override
	protected void drawShapeLine(Graphics2D g)
	{
		g.draw(_line);
		switch (_direction)
		{
		case Default:
			drawDefaultDirection(g);
			break;
		case Other:
			drawOtherDirection(g);
			break;
		case Both:
			drawDefaultDirection(g);
			drawOtherDirection(g);
			break;
		}
	}
	
	private void drawDefaultDirection(Graphics2D g)
	{
		final double arrowLength = 7;
		final double spacing = 10;
		Point2D center = getCenter();
		double lineAngle = _line.x1 == _line.x2 ? Math.PI/2 : Math.atan((_line.y2-_line.y1)/(_line.x2-_line.x1));
		if (_line.x2 < _line.x1) lineAngle += Math.PI;
		center = new Point2D.Double(
			center.getX() + spacing * Math.cos(lineAngle),
			center.getY() + spacing * Math.sin(lineAngle));
		double arrowAngle1 = lineAngle + Math.PI * 3/4;
		Point2D arrowPoint1 = new Point2D.Double(
			center.getX() + arrowLength * Math.cos(arrowAngle1), 
			center.getY() + arrowLength * Math.sin(arrowAngle1));
		double arrowAngle2 = arrowAngle1 + Math.PI / 2;
		Point2D arrowPoint2 = new Point2D.Double(
			center.getX() + arrowLength * Math.cos(arrowAngle2), 
			center.getY() + arrowLength * Math.sin(arrowAngle2));
		g.draw(new Line2D.Double(center, arrowPoint1));
		g.draw(new Line2D.Double(center, arrowPoint2));
	}
	
	private void drawOtherDirection(Graphics2D g)
	{
		final double arrowLength = 7;
		final double spacing = 10;
		Point2D center = getCenter();
		double lineAngle = _line.x1 == _line.x2 ? Math.PI/2 : Math.atan((_line.y2-_line.y1)/(_line.x2-_line.x1));
		if (_line.x2 < _line.x1) lineAngle += Math.PI;
		center = new Point2D.Double(
			center.getX() - spacing * Math.cos(lineAngle),
			center.getY() - spacing * Math.sin(lineAngle));
		double arrowAngle1 = lineAngle - Math.PI/4;
		Point2D arrowPoint1 = new Point2D.Double(
			center.getX() + arrowLength * Math.cos(arrowAngle1), 
			center.getY() + arrowLength * Math.sin(arrowAngle1));
		double arrowAngle2 = arrowAngle1 + Math.PI / 2;
		Point2D arrowPoint2 = new Point2D.Double(
			center.getX() + arrowLength * Math.cos(arrowAngle2), 
			center.getY() + arrowLength * Math.sin(arrowAngle2));
		g.draw(new Line2D.Double(center, arrowPoint1));
		g.draw(new Line2D.Double(center, arrowPoint2));
	}

	@Override
	protected void drawShapeEmphasis(Graphics2D g)
	{
		g.setStroke(new BasicStroke(getLineWidth() + 2));
		g.draw(_line);
	}

	@Override
	protected void doMove(double dx, double dy)
	{
		if (_vertex1 == null)
		{
			_line.x1 += dx;
			_line.y1 += dy;
		}
		if (_vertex2 == null)
		{
			_line.x2 += dx;
			_line.y2 += dy;
		}
	}

	@Override
	public boolean contains(double x, double y)
	{
		return _line.ptSegDist(x,y) <= 1;
	}

	@Override
	public void shapeMoved(ShapeMovedEvent e)
	{
		if (e.getShape().equals(_vertex1))
		{
			_line.x1 = _vertex1.getCenter().getX();
			_line.y1 = _vertex1.getCenter().getY();
		}
		else if (e.getShape().equals(_vertex2))
		{
			_line.x2 = _vertex2.getCenter().getX();
			_line.y2 = _vertex2.getCenter().getY();	
		}
	}

	@Override
	public Point2D getCenter()
	{
		return new Point2D.Double((_line.x1 + _line.x2)/2, (_line.y1 + _line.y2)/2);
	}
	
	@Override
	public String toString()
	{
		String txt1 = _vertex1 != null ? _vertex1.getText() : "";
		String txt2 = _vertex2 != null ? _vertex2.getText() : "";
		String op = "---";
		if (_direction != null)
		{
			switch (_direction)
			{
			case Both:
				op = "<->"; 
				break;
			case Default:
				op = "-->"; 
				break;
			case None:
				op = "---"; 
				break;
			case Other:
				op = "<--"; 
				break;
			}
		}
		return String.format("%s %s %s", txt1, op, txt2);
	}

	@Override
	public Dimension getDimension()
	{
		int width = (int)Math.abs(_line.x2 - _line.x1);
		int height = (int)Math.abs(_line.y2 - _line.y1);
		return new Dimension(width, height);
	}

}
