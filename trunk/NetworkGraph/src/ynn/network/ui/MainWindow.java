package ynn.network.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ynn.network.adapter.NetworkAdapter;
import ynn.network.model.NetworkModel;
import ynn.network.model.Node;
import ynn.network.ui.ConnectorShape.Direction;
import ynn.network.ui.NetworkView.Mode;
import ynn.network.util.DeserializationException;
import ynn.network.util.NetworkSerializer;
import ynn.network.util.SerializationException;
import ynn.tech.algorithms.network.AlgorithmDescriptor;
import ynn.tech.algorithms.network.aky90.Aky90Descriptor;

public class MainWindow extends JFrame
{
    private static final long serialVersionUID = 3198885837872604187L;
    private int _nodeId = 1;
    private NetworkView _networkView;
    private NetworkModel _networkModel;
    private NetworkAdapter _networkAdapter; 
    
    public MainWindow()
    {
        super();
        setSize(500, 300);
        setLocationRelativeTo(null);
        setTitle("Main Window");
        _networkModel = new NetworkModel();
        _networkView = new NetworkView();
        _networkAdapter = new NetworkAdapter();
        _networkAdapter.attach(_networkModel);
        _networkAdapter.attach(_networkView);
        setContentPane(_networkView);
        final AlgorithmDescriptor algDescriptor = new Aky90Descriptor();
        addKeyListener(new KeyAdapter()
		{	
			@Override
			public void keyPressed(KeyEvent e)
			{
				switch (e.getKeyCode())
				{
				case KeyEvent.VK_V:
					_networkView.setMode(Mode.View);
					break;
				case KeyEvent.VK_M:
					_networkView.setMode(Mode.Move);
					break;
				case KeyEvent.VK_C:
					_networkView.setMode(Mode.Connect);
					break;
				case KeyEvent.VK_INSERT:
					NodeShape shape = new NodeShape();
					shape.setText(String.valueOf(_nodeId++));
					Node node = algDescriptor.getUtilities().createNode();
					node.setName(shape.getText());
					algDescriptor.getUtilities().initNodeAttributes(node);
					shape.setData(node);
					_networkView.addShape(shape);
					break;
				case KeyEvent.VK_DELETE:
					_networkView.removeSelectedShape();
					break;
				case KeyEvent.VK_P:
					_networkAdapter.getClass();
					break;
				case KeyEvent.VK_S:
					try
					{
						File file = new File("c:\\Temp\\model.xml");
						NetworkSerializer serializer = new NetworkSerializer(_networkView);
						serializer.serialize(file);
					}
					catch (SerializationException ex)
					{
						ex.printStackTrace();
					}
					break;
				case KeyEvent.VK_L:
					try
					{
						File file = new File("c:\\Temp\\model.xml");
						NetworkSerializer serializer = new NetworkSerializer(_networkView);
						serializer.deserialize(file);
						_networkView.repaint();
					}
					catch (DeserializationException ex)
					{
						ex.printStackTrace();
					}
					break;
				case KeyEvent.VK_D:
					AbstractShape[] selectedShapes = _networkView.getSelectedShapes();
					for (AbstractShape selShape : selectedShapes)
					{
						if (selShape instanceof ConnectorShape)
						{
							ConnectorShape conn = (ConnectorShape)selShape;
							Direction direction = conn.getDirection();
							direction = Direction.values()[(direction.ordinal() + 1) % Direction.values().length];
							_networkView.setConnectorsDirection(new ConnectorShape[] { conn }, direction);
						}
					}
					_networkView.repaint();
					break;
				}
			}
		});
    }


    public static void main(String[] args)
    {
        try
        {
            SwingUtilities.invokeAndWait(new Runnable()
            {
                @Override
                public void run()
                {
                    MainWindow window = new MainWindow();
                    window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    window.setVisible(true);
                }
            });
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

}
