package ynn.network.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractShape implements IAnnotationProvider
{
	private boolean _isSelected = false;
	private String _text = "";
	private Color _fillColor = Color.GREEN;
	private Color _hoverColor = Color.BLUE;
	private Color _selectedColor = Color.YELLOW;
	private Color _lineColor = Color.BLACK;
	private Color _textColor = Color.BLACK;
	private Color _annLineColor = Color.BLACK;//getHSBColor(38, 240, 209);
	private Color _annFillColor = Color.YELLOW;//getHSBColor(26, 240, 209);
	private Color _annTextColor = Color.BLACK;//Color.getHSBColor(12, 107, 102);
	private float _lineWidth = 1.0f;
	private Font _font = Font.decode("Arial").deriveFont(Font.BOLD, 20.0f);
	private Font _annFont = Font.decode("Arial").deriveFont(Font.PLAIN, 14.0f);
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

	protected abstract void drawAnnotationLine(Graphics2D g);

	protected abstract void drawAnnotationFill(Graphics2D g);

	protected abstract void drawAnnotationText(Graphics2D g);

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
		if (getAnnotationText() != null && !getAnnotationText().isEmpty())
		{
			g.setColor(_annFillColor);
			drawAnnotationFill(g);
			g.setColor(_annTextColor);
			g.setFont(getAnnotationFont());
			drawAnnotationText(g);
			g.setColor(_annLineColor);
			g.setStroke(new BasicStroke(getLineWidth()));
			drawAnnotationLine(g);
		}
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

	public void setAnnotationTextColor(Color color)
	{
		_annTextColor = color;
	}

	public Color getAnnotationTextColor()
	{
		return _annTextColor;
	}

	public void setAnnotationFillColor(Color color)
	{
		_annFillColor = color;
	}

	public Color getAnnotationFillColor()
	{
		return _annFillColor;
	}

	public void setAnnotationLineColor(Color color)
	{
		_annLineColor = color;
	}

	public Color getAnnotationLineColor()
	{
		return _annLineColor;
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

	public void setAnnotationFont(Font font)
	{
		_annFont = font;
	}

	public Font getAnnotationFont()
	{
		return _annFont;
	}
	
	public void setData(Object data)
	{
		_data = data;
	}
	
	public Object getData()
	{
		return _data;
	}
	
	public abstract Dimension getDimension();
	
	public String getAnnotationText()
	{
		return getData() != null && getData() instanceof IAnnotationProvider ?
			((IAnnotationProvider)getData()).getAnnotationText() : null;
	}
}
