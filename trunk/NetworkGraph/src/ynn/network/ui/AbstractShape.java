package ynn.network.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractShape
{
	private boolean _isSelected = false;
	private String _text = "";
	private Color _fillColor = Color.GREEN;
	private Color _hoverColor = Color.BLUE;
	private Color _selectedColor = Color.YELLOW;
	private Color _lineColor = Color.BLACK;
	private Color _textColor = Color.BLACK;
	private float _lineWidth = 1.0f;
	private Font _font = Font.decode("Arial").deriveFont(Font.BOLD, 20.0f);
	private List<IShapeListener> _shapeListeners = new LinkedList<IShapeListener>();
	private Object _data = null;
	
	public void dispose()
	{
		_shapeListeners.clear();
	}
	
	public void addShapeListener(IShapeListener l)
	{
		if (!_shapeListeners.contains(l))
			_shapeListeners.add(l);
	}
	
	public void removeShapeListener(IShapeListener l)
	{
		if (_shapeListeners.contains(l))
			_shapeListeners.remove(l);	
	}
	
	public void fireShapeMoved(Point2D oldPoint, Point2D newPoint)
	{
		ShapeMovedEvent e = new ShapeMovedEvent(this, oldPoint, newPoint);
		for (IShapeListener l : _shapeListeners)
		{
			l.shapeMoved(e);
		}
	}
	
	public void fireShapeMoved(Point2D deltaPoint)
	{
		ShapeMovedEvent e = new ShapeMovedEvent(this, deltaPoint);
		for (IShapeListener l : _shapeListeners)
		{
			l.shapeMoved(e);
		}
	}
	
	public List<IShapeListener> getShapeListeners()
	{
		return _shapeListeners;
	}

	protected abstract void drawShapeFill(Graphics2D g);

	protected abstract void drawShapeLine(Graphics2D g);

	protected void drawShapeText(Graphics2D g)
	{
	}

	protected abstract void drawShapeEmphasis(Graphics2D g);

	public void draw(Graphics2D g, boolean isHovered)
	{
		if (isHovered)
		{
			g.setColor(_hoverColor);
			drawShapeEmphasis(g);
		}
		else if (isSelected())
		{
			g.setColor(_selectedColor);
			drawShapeEmphasis(g);
		}
		g.setColor(_fillColor);
		drawShapeFill(g);
		g.setColor(_lineColor);
		g.setStroke(new BasicStroke(getLineWidth()));
		drawShapeLine(g);
		g.setColor(_textColor);
		g.setFont(getFont());
		drawShapeText(g);
	}
	
	public void draw(Graphics2D g)
	{
		draw(g,false);
	}
	
	protected abstract void doMove(double dx, double dy);
	
	public final void move(double dx, double dy)
	{
		doMove(dx, dy);
		fireShapeMoved(new Point2D.Double(dx,dy));
	}
	
	public final void move(Point2D dp)
	{
		move(dp.getX(), dp.getY());
	}
	
	public abstract Point2D getCenter();
	
	public abstract boolean contains(double x, double y);
	
	public boolean contains(Point2D p)
	{
		return contains(p.getX(), p.getY());
	}

	public void setSelected(boolean selected)
	{
		_isSelected = selected;
	}

	public boolean isSelected()
	{
		return _isSelected;
	}

	public void setText(String text)
	{
		_text = text;
	}

	public String getText()
	{
		return _text;
	}

	public void setFillColor(Color color)
	{
		_fillColor = color;
	}

	public Color getFillColor()
	{
		return _fillColor;
	}

	public void setLineColor(Color color)
	{
		_lineColor = color;
	}

	public Color getLineColor()
	{
		return _lineColor;
	}

	public void setSelectedColor(Color color)
	{
		_selectedColor = color;
	}

	public Color getSelectedColor()
	{
		return _selectedColor;
	}

	public void setHoverColor(Color color)
	{
		_hoverColor = color;
	}

	public Color getHoverColor()
	{
		return _hoverColor;
	}

	public void setTextColor(Color color)
	{
		_textColor = color;
	}

	public Color getTextColor()
	{
		return _textColor;
	}

	public void setLineWidth(float width)
	{
		_lineWidth = width;
	}

	public float getLineWidth()
	{
		return _lineWidth;
	}

	public void setFont(Font font)
	{
		_font = font;
	}

	public Font getFont()
	{
		return _font;
	}
	
	public void setData(Object data)
	{
		_data = data;
	}
	
	public Object getData()
	{
		return _data;
	}
}
