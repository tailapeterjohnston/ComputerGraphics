/**
   A program that demonstrates how Java can use OpenGL commands to
   display a solid torus via the JOGL binding (modified for JSR-231)
*/
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import com.sun.opengl.util.GLUT;

public class TorusJOGL implements GLEventListener
{
   private static final int WIDTH = 640; // initial canvas width
   private static final int HEIGHT = 480; // initial canvas height
   private GLU glu;
   private GLUT glut; // the GLUT utility library used for torus
   private GLCanvas canvas;
   
   public TorusJOGL(GLCanvas canvas)
   {  this.glu = new GLU();
      glut = new GLUT();
      this.canvas = canvas;
   }
   
   // called when OpenGL is initialized
   public void init(GLAutoDrawable drawable)
   {  GL gl = drawable.getGL();
      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // black background

      // material characteristics
      float[] ambientMaterial = {1.0f, 0.0f, 0.0f, 0.0f}; // red colour
      float[] shininessMaterial = {100.0f};
      float[] specularMaterial = {3000.0f, 3000.0f, 3000.0f, 3000.0f};
      gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, ambientMaterial, 0);
      gl.glMaterialfv(GL.GL_FRONT,GL.GL_SHININESS,shininessMaterial,0);
      gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specularMaterial,0);
  
      // light characteristics
      float[] ambientLight = {0.2f, 0.2f, 0.2f, 1.0f};
      float[] diffuseLight = {1.0f, 1.0f, 1.0f, 1.0f}; // white colour
      float[] specularLight = {1.0f, 1.0f, 1.0f, 1.0f}; // white colour
      float[] positionLight = {0.0f, 3.0f, 3.0f, 0.0f};
      gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0);
      gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0);
      gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, positionLight, 0);
      gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specularLight, 0);

      gl.glShadeModel(GL.GL_SMOOTH);
      gl.glEnable(GL.GL_LIGHTING);
      gl.glEnable(GL.GL_LIGHT0);
      gl.glEnable(GL.GL_AUTO_NORMAL);
      gl.glEnable(GL.GL_NORMALIZE);
      gl.glEnable(GL.GL_DEPTH_TEST);
   }
   
   // draws the torus
   public void display(GLAutoDrawable drawable)
   {  GL gl = drawable.getGL();
      // position and aim the camera with its eye at (2,2,2), 
      // central look point (0,0,0), up direction along y-axis (0,1,0)
      gl.glMatrixMode(GL.GL_MODELVIEW);
      gl.glLoadIdentity();
      glu.gluLookAt(2.0, 2.0, 2.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
       // clear the screen and draw the torus
      gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
      glut.glutSolidTorus(0.5, 1, 40, 40);
      gl.glFlush();
   }
   
   // called when component's location or size has changed
   public void reshape(GLAutoDrawable drawable, int x, int y,
      int width, int height)
   {  GL gl = drawable.getGL();
      // set view volume shape so camera has view volume from -2 to +2
      // aspect ratio WIDTH/HEIGHT, near plane at 0.1,far plane at 100
      gl.glViewport (0, 0, width, height);
      gl.glMatrixMode(GL.GL_PROJECTION);
      gl.glLoadIdentity();
      gl.glOrtho(-2.0*WIDTH/HEIGHT,2.0*WIDTH/HEIGHT,-2.0,2.0,0.1,100);
      canvas.repaint();
   }
   
   // called when display mode of device has changed
   public void displayChanged(GLAutoDrawable drawable,
      boolean modeChanged, boolean deviceChanged)
   {  // empty implementation
   }
   
   public static void main(String[] args)
   {  // prepare an OpenGL canvas suitable for capabilities of display
      GLCapabilities capabilities = new GLCapabilities();
      GLCanvas canvas = new GLCanvas(capabilities);
      canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
      canvas.addGLEventListener(new TorusJOGL(canvas));
      // prepare a frame to hold the canvas
      JFrame frame = new JFrame("An OpenGL Torus in Java");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(canvas);
      frame.pack();
      // position the frame in the middle of the screen
      Toolkit tk = Toolkit.getDefaultToolkit();
      Dimension screenDimension = tk.getScreenSize();
      Dimension frameDimension = frame.getSize();
      frame.setLocation((screenDimension.width-frameDimension.width)/2,
         (screenDimension.height-frameDimension.height)/2);
      frame.setVisible(true);
   }
}
