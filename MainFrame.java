import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;


public class MainFrame extends JFrame {

	private JPanel contentPane;
	private BufferedImage image = null;
	private ImageProcessing ip = new ImageProcessing();
	private ImageIcon imIcon = null;
	private int[] pixels;
	//private boolean pixelFlag = false;
	private int iw, ih;
	private LinearTransDialog dialog = null;
	private GrayStretchDialog dialog2 = null;
	
	private String orginFilePath;
	
	private int MOSAICSIZE = 32;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("11061151 \u5E9E\u68A6\u52BC \u6570\u5B57\u56FE\u50CF\u5904\u7406");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 450);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
//		mntmSave.setEnabled(false);
		mnFile.add(mntmSave);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmGray = new JMenuItem("Gray");
		mnEdit.add(mntmGray);
		
		JMenuItem mntmLinetrans = new JMenuItem("Linear Transformation");
		mnEdit.add(mntmLinetrans);
		
		JMenuItem mntmShow = new JMenuItem("Show Histogram");
		mnEdit.add(mntmShow);
		
		JMenuItem mntmHistogramEqualization = new JMenuItem("Histogram Equalization");
		mnEdit.add(mntmHistogramEqualization);
		
		JMenuItem mntmGrayStretch = new JMenuItem("Gray Stretch");
		mnEdit.add(mntmGrayStretch);
		
		JMenuItem mntmFft = new JMenuItem("FFT");
		mnEdit.add(mntmFft);
		
		JMenuItem mntmDct = new JMenuItem("DCT");
		mnEdit.add(mntmDct);
		
		JMenuItem mntmEdgeExtractioncanny = new JMenuItem("Edge Extraction (Canny) ");
		mnEdit.add(mntmEdgeExtractioncanny);
		
		JMenuItem mntmEdgeExtractionsobel = new JMenuItem("Edge Extraction (Sobel)");
		mnEdit.add(mntmEdgeExtractionsobel);
		
		JMenuItem mntmAddMosaic = new JMenuItem("Add Mosaic");
		mnEdit.add(mntmAddMosaic);
		
		JMenuItem mntmRemoveMosaic = new JMenuItem("Remove Mosaic");
		mnEdit.add(mntmRemoveMosaic);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		final JLabel lblImage = new JLabel("");
		scrollPane.setViewportView(lblImage);
		
		mntmOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Open File");
				JFileChooser fChooser = new JFileChooser();  
				//fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  
				fChooser.showOpenDialog(null);  
				File f = fChooser.getSelectedFile();
				orginFilePath = f.getAbsolutePath();
				System.out.println("orgin file path: " + orginFilePath);
				if(f != null)
				{
					try {
						image = ImageIO.read(f);
						iw = image.getWidth();
						ih = image.getHeight();
						System.out.println("Opened");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					pixels = ip.imageToIntAarry(image);
					imIcon = new ImageIcon();
					imIcon.setImage(image);
					lblImage.setIcon(imIcon);
					
				}
			}
		});
		
		mntmSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Save File");
				JFileChooser jf = new JFileChooser(); 
				jf.setFileSelectionMode(JFileChooser.SAVE_DIALOG | JFileChooser.DIRECTORIES_ONLY);  
				jf.showDialog(null,null);  
				File fi = jf.getSelectedFile();  
				String f = fi.getAbsolutePath()+"/test.jpg"; 
				String f2 = jf.getName(fi); 
				System.out.println("f2 is: " + f2);
				System.out.println("save to: " + f);  
				
				try { 
					//将BufferedImage变量写入文件中。
		            ImageIO.write(image,"bmp",new File(f)); 
		        } catch (IOException ioe) { 
		            // TODO Auto-generated catch block 
		            ioe.printStackTrace(); 
		        } 
			}
		});
		
		mntmGray.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Gray");
				
				pixels = ip.imageToIntAarry(image);
				
				pixels = ip.toGray(pixels, iw, ih);
				
				ImageProducer mis = new MemoryImageSource(iw, ih, pixels, 0, iw);
				Image im = createImage(mis);
				
				//Image -> BufferedImage
				Graphics g = image.createGraphics();  
				g.drawImage(im, 0, 0, null);  
				g.dispose();
				imIcon = new ImageIcon();
				imIcon.setImage(image);
				lblImage.setIcon(imIcon);
			}
		});
		
		mntmLinetrans.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Linear Transformation");
				
				double a = 1.5;
				double b = 5;
				double [] para = null;
				
				if(dialog == null)
					dialog = new LinearTransDialog();
				
				//set default parameters
				dialog.setPara("1.5", "5");
				
				if(dialog.showDialog(MainFrame.this, "Set Parameters"))
				{
					para = dialog.getPara();
					a = para[0];
					b = para[1];
					System.out.println("1a = " + a + " b = " + b);
					
					pixels = ip.imageToIntAarry(image);
					pixels = ip.linearTrans(pixels, iw, ih, a, b);
					
					
					ImageProducer mis = new MemoryImageSource(iw, ih, pixels, 0, iw);
					Image im =  createImage(mis);
					
					//Image -> BufferedImage
					Graphics g = image.createGraphics();  
					g.drawImage(im, 0, 0, null);  
					g.dispose();
					imIcon = new ImageIcon();
					imIcon.setImage(image);
					lblImage.setIcon(imIcon);
					
				}
			}
		});
		
		mntmGrayStretch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Gray Stretch");
				
				int r1, s1, r2, s2;
				int [] para = null;
				
				if(dialog2 == null)
					dialog2 = new GrayStretchDialog();
				
				//set default parameters
				dialog2.setPara("40", "60", "200", "220");
				
				if(dialog2.showDialog(MainFrame.this, "Set Parameters"))
				{
					para = dialog2.getPara();
					r1 = para[0];
					s1 = para[1];
					r2 = para[2];
					s2 = para[3];
					System.out.println("r1 = " + r1 + " s1 = " + s1 + " r2 = " + r2 + " s2 = " + s2);
					
					pixels = ip.imageToIntAarry(image);
					pixels = ip.grayStretch(pixels, iw, ih, r1, s1, r2, s2);
					
					
					ImageProducer mis = new MemoryImageSource(iw, ih, pixels, 0, iw);
					Image im =  createImage(mis);
					
					//Image -> BufferedImage
					Graphics g = image.createGraphics();  
					g.drawImage(im, 0, 0, null);  
					g.dispose();
					imIcon = new ImageIcon();
					imIcon.setImage(image);
					lblImage.setIcon(imIcon);
				}
			}
		});
		
		mntmShow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Show Histogram");
				
				pixels = ip.imageToIntAarry(image);
				
				BufferedImage histogramImage = ip.showHistogram(ip.getHistogram(pixels, iw, ih));
				
				ImageProducer mis = new MemoryImageSource(iw, ih, pixels, 0, iw);
				Image im = createImage(mis);
				
				JFrame histoFrame = new JFrame("Histogram");
				JPanel histoPanel = new JPanel();
				histoFrame.setContentPane(histoPanel);
				JLabel histoLabel = new JLabel();
				histoPanel.add(histoLabel);
				histoFrame.setSize(new Dimension(350, 350));
				histoFrame.setResizable(false);
				histoFrame.setVisible(true);
				
				ImageIcon histoIcon = new ImageIcon();
				histoIcon.setImage(histogramImage);
				histoLabel.setIcon(histoIcon);
				
			}
		});
		
		mntmHistogramEqualization.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Histogram Equalization");
				
				pixels = ip.imageToIntAarry(image);
				pixels = ip.histogramEqualization(pixels, iw, ih);
				
				ImageProducer mis = new MemoryImageSource(iw, ih, pixels, 0, iw);
				Image im =  createImage(mis);
				
				//Image -> BufferedImage
				Graphics g = image.createGraphics();  
				g.drawImage(im, 0, 0, null);  
				g.dispose();
				imIcon = new ImageIcon();
				imIcon.setImage(image);
				lblImage.setIcon(imIcon);
			}
		});
		
		mntmFft.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("FFT");
				
				checkSqr(lblImage);
				
				Complex[] fftData;
				int[] iPix = new int[iw * ih];
				pixels = ip.imageToIntAarry(image);
	        	    
	        	for (int j = 0; j < iw * ih; j++)
	        		iPix[j] = pixels[j]&0xff;                      
                //FFT变换
                FFT2 fft2 = new FFT2(iPix, iw, ih);
                fft2.doFFT2();
                fftData = fft2.getFFT2();
                
                //FFT数据可视化
                int[] fftPixels = fft2.toPix(fftData, iw, ih);
                
                ImageProducer mis = new MemoryImageSource(iw, ih, fftPixels, 0, iw);
                BufferedImage fftBI = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
				Image im =  createImage(mis);
				
				//Image -> BufferedImage
				Graphics g = fftBI.createGraphics();  
				g.drawImage(im, 0, 0, null);  
				g.dispose();
				
				// Save to file
				try { 
		            ImageIO.write(fftBI,"bmp",new File(orginFilePath + "FFT.bmp"));
		        } catch (IOException ioe) { 
		            // TODO Auto-generated catch block 
		            ioe.printStackTrace(); 
		        } 
                
                JFrame fftFrame = new JFrame("Fast Fourier Transform");
                JPanel fftPanel = new JPanel();
                fftFrame.setContentPane(fftPanel);
				JLabel fftLabel = new JLabel();
				fftPanel.add(fftLabel);
				fftFrame.setSize(new Dimension(iw + 20, ih + 20));
				fftFrame.setResizable(false);
				fftFrame.setVisible(true);
				
				ImageIcon fftIcon = new ImageIcon();
				fftIcon.setImage(fftBI);
				fftLabel.setIcon(fftIcon);
                
//				}
//				else
//				{
					
//				}
			}
		});
		
		mntmDct.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("DCT");
				
				checkSqr(lblImage);
				/*boolean flag = false;
				int i = 1;
				if(iw == ih)
				{
					while(i <= iw)
					{
						if(i == iw)
							flag = true;
						i *= 2;
					}
				}
				else
					flag = false;
				
				if(!flag)
				{
					JOptionPane.showMessageDialog(null, "图像将被压缩为尺寸为2的整数次方的图像！");
					int s;
					if(iw < ih)
						s = iw;
					else
						s = ih;
					i = 1;
					while(i <= s)
					{
						i *= 2;
					}
					s = i;
					iw = s;
					ih = s;
					//image = ip.cut(image, 0, 0, s, s);
					image = ip.resize(image, s, s, false);
					imIcon = new ImageIcon();
					imIcon.setImage(image);
					lblImage.setIcon(imIcon);
				}*/
				
//				if(flag && iw >= 128)
//				{
					int bsize = 256;
					double[][] dctData;
					
					double[][] iPix = new double[ih][iw];//输入灰度
	                pixels = ip.imageToIntAarry(image);
	        	    
	        	    for (int j = 0; j < ih; j++)
	                    for (int k = 0; k < iw; k++)
	                        iPix[k][j] = pixels[k+j*iw] & 0xff;                  
	                    
	               //DCT变换
	                DCT2 dct2 = new DCT2();
	                dctData = dct2.dctTrans(iPix, iw, ih, bsize);
	                	                
	                //DCT数据可视化
	                int[] dctPixels = dct2.toPixels(dctData, iw, ih);
	                
	                ImageProducer mis = new MemoryImageSource(iw, ih, dctPixels, 0, iw);
	                BufferedImage dctBI = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
					Image im =  createImage(mis);
					
					//Image -> BufferedImage
					Graphics g = dctBI.createGraphics();  
					g.drawImage(im, 0, 0, null);  
					g.dispose();
	                
	                JFrame dctFrame = new JFrame("Discrete Cosine Transform");
	                JPanel dctPanel = new JPanel();
	                dctFrame.setContentPane(dctPanel);
					JLabel dctLabel = new JLabel();
					dctPanel.add(dctLabel);
					dctFrame.setSize(new Dimension(iw + 20, ih + 20));
					dctFrame.setResizable(false);
					dctFrame.setVisible(true);
					
					ImageIcon dctIcon = new ImageIcon();
					dctIcon.setImage(dctBI);
					dctLabel.setIcon(dctIcon);
	                
//				}
//				else
//				{
//					JOptionPane.showMessageDialog(null, "本程序仅适用于尺寸为2的整数次方的图像！");
//				}
			}
		});
		
		mntmEdgeExtractioncanny.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Edge Extraction (Canny)");
				
//				EdgeDetector edgeDetector=new EdgeDetector();
//		        edgeDetector.setSourceImage(image);
//		        edgeDetector.setThreshold(128);
//		        edgeDetector.setWidGaussianKernel(5);
//
//		        edgeDetector.process();
//		        
//		        Image cannyImage = edgeDetector.getEdgeImage();
		        
//				//create the detector
//				CannyEdgeDetector detector = new CannyEdgeDetector();
//				//adjust its parameters as desired
//				detector.setLowThreshold(2.5f);
//				detector.setHighThreshold(5f);
//				//apply it to an image
//				detector.setSourceImage(image);
//				detector.process();
//				BufferedImage cannyImage = detector.getEdgesImage();
		        
				pixels = ip.imageToIntAarry(image);
				
				pixels = ip.toGray(pixels, iw, ih);
				BufferedImage cannyImage = ip.cannyEdgeImage(pixels, iw, ih);
				
		        imIcon = new ImageIcon();
				imIcon.setImage(cannyImage);
				lblImage.setIcon(imIcon);
			}
		});
		
		mntmEdgeExtractionsobel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Edge Extraction (Sobel)");
				
				pixels = ip.imageToIntAarry(image);
				
				pixels = ip.toGray(pixels, iw, ih);
				
				BufferedImage sobelBImage = ip.sobelEdgeImage(pixels, iw, ih, 40);
		        
		        imIcon = new ImageIcon();
				imIcon.setImage(sobelBImage);
				lblImage.setIcon(imIcon);
			}
		});
		
		mntmAddMosaic.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Add Mosaic");
				
				checkSqr(lblImage);
				
				pixels = ip.imageToIntAarry(image);
				
				pixels = ip.addMosaic(pixels, iw, ih, MOSAICSIZE);
				
				ImageProducer mis = new MemoryImageSource(iw, ih, pixels, 0, iw);
				Image im = createImage(mis);
				
				//Image -> BufferedImage
				Graphics g = image.createGraphics();  
				g.drawImage(im, 0, 0, null);  
				g.dispose();
				imIcon = new ImageIcon();
				imIcon.setImage(image);
				lblImage.setIcon(imIcon);
			}
		});
		
		mntmRemoveMosaic.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Add Mosaic");
				
				pixels = ip.imageToIntAarry(image);
				
				pixels = ip.removeMosaic(pixels, iw, ih, MOSAICSIZE);
				
				ImageProducer mis = new MemoryImageSource(iw, ih, pixels, 0, iw);
				Image im = createImage(mis);
				
				//Image -> BufferedImage
				Graphics g = image.createGraphics();  
				g.drawImage(im, 0, 0, null);  
				g.dispose();
				imIcon = new ImageIcon();
				imIcon.setImage(image);
				lblImage.setIcon(imIcon);
			}
		});
	}
	
	private void checkSqr(JLabel lblImage){
		boolean flag = false;
		int i = 1;
		if(iw == ih)
		{
			while(i <= iw)
			{
				if(i == iw)
					flag = true;
				i *= 2;
			}
		}
		else
			flag = false;
		
		
		if(!flag)
		{
			JOptionPane.showMessageDialog(null, "图像将被压缩为尺寸为2的整数次方的图像！");
			int s;
			if(iw < ih)
				s = iw;
			else
				s = ih;
			i = 1;
			while(i <= s)
			{
				i *= 2;
			}
			s = i;
			iw = s;
			ih = s;
			//image = ip.cut(image, 0, 0, s, s);
			image = ip.resize(image, s, s, false);
			imIcon = new ImageIcon();
			imIcon.setImage(image);
			lblImage.setIcon(imIcon);
		}
	}
}
