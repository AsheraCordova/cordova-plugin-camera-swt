package org.apache.cordova.camera;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.JRootPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;


/**
 * Example of how WebcamPanel can be used in SWT using SWT_AWT bridge.
 * 
 * @author Dimitrios Chondrokoukis
 */
public class WebCameraPanel {
	private Webcam webcam;
	private WebcamPanel panel;
	private CameraLauncher cameraLauncher;
	private Composite composite;
	public Composite getPanelComposite() {
		return composite;
	}
	
	public static String imgToBase64String(final RenderedImage img, final String formatName) {
	    final ByteArrayOutputStream os = new ByteArrayOutputStream();
	    try (final OutputStream b64os = Base64.getEncoder().wrap(os)) {
	        ImageIO.write(img, formatName, b64os);
	    } catch (final IOException ioe) {
	        throw new UncheckedIOException(ioe);
	    }
	    return os.toString();
	}

	/**
	 * Create the shell.
	 * @param qrScanner 
	 * 
	 * @param display
	 */
	public WebCameraPanel(CameraLauncher cameraLauncher, Composite qrcodePanel) {
		this.cameraLauncher = cameraLauncher;
		Dimension size = WebcamResolution.QVGA.getSize();

		this.composite = new Composite(qrcodePanel, SWT.EMBEDDED);
		composite.setBounds(0, 0, qrcodePanel.getBounds().width, qrcodePanel.getBounds().height);
		Frame frame = SWT_AWT.new_Frame(composite);

		JRootPane root = new JRootPane();
		frame.add(root);

		webcam = Webcam.getDefault();
		webcam.setViewSize(size);

		panel = new WebcamPanel(webcam, size, false);
		panel.setFPSDisplayed(true);
		root.getContentPane().add(panel);
		javax.swing.JButton captureButton = new javax.swing.JButton("Capture");
		panel.add(captureButton);
		this.composite.addDisposeListener((e) -> panel.stop());
		
		captureButton.addMouseListener(new java.awt.event.MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				BufferedImage image = webcam.getImage();
				System.out.println(imgToBase64String(image,  "png"));
				cameraLauncher.resultCallBack(imgToBase64String(image,  "png"));
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
			
		});
	}


	public void start() {
		panel.start();
	}
	

	public void close() {
		webcam.close();
	}
}