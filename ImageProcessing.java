import java.awt.*;
import java.awt.color.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.util.Arrays;

import javax.imageio.*;
import javax.swing.*;


public class ImageProcessing {
	private BufferedImage bImage = null;
	
	public ImageProcessing()
	{
		System.out.println("ImageProcessing");
	}
	
	public BufferedImage gray(BufferedImage image) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);
		image = op.filter(image, null);
		//ImageIO.write(src, "JPEG", new File(destImageFile));
		return image;
    }
	
	public int [] toGray(int [] pixels, int w, int h)
	{
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b, gray;
		
		for(int i=0; i<w*h; i++)
		{
			r = cm.getRed(pixels[i]);
			g = cm.getGreen(pixels[i]);
			b = cm.getBlue(pixels[i]);
			gray = (int)((r + g + b) / 3);
			pixels[i] = 255 << 24 | gray << 16 | gray << 8 | gray;
		}
		return pixels;
	}
	
	public int [] imageToIntAarry(BufferedImage image)
	{
		int w = image.getWidth();
		int h = image.getHeight();
		int[] pixels = new int[w * h];
		PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pixels;
	}
	
	
	public int [] linearTrans(int [] pixels, int w, int h, double k, double a)
	{
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b;
		int gray;
		for(int i=0; i<w*h; i++)
		{
			r = cm.getRed(pixels[i]);
			g = cm.getGreen(pixels[i]);
			b = cm.getBlue(pixels[i]);
			
			r = (int)(k * r + a);
			g = (int)(k * g + a);
			b = (int)(k * b + a);
			
			if(r > 255) r = 255;
			if(g > 255) g = 255;
			if(b > 255) b = 255;
			
			gray = (int)((r + g + b) / 3);
			pixels[i] = 255 << 24 | gray << 16 | gray << 8 | gray;
		}
		
		return pixels;
	}
	
	public int [] grayStretch(int [] pixels, int w, int h, double r1, double s1, double r2, double s2)
	{
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b;
		double k1, k2, k3, a1, a2, a3;
		int gray;
		
		k1 = s1 / r1;
		/*if(r1 == r2)
			k2 = k1;
		else
			k2 = (s2 - s1) / (r2 - r1);
		if(r2 == 255 && s2 == 255)
			k3 = 0;
		else
			k3 = (255 - s2) / (255 - r2);*/
		k2 = (s2 - s1) / (r2 - r1);
		k3 = (255 - s2) / (255 - r2);
		a1 = 0;
		a2 = s1;
		a3 = s2;
		for(int i=0; i<w*h; i++)
		{
			r = cm.getRed(pixels[i]);
			g = cm.getGreen(pixels[i]);
			b = cm.getBlue(pixels[i]);
			
			if(r<r1)
				r = (int)(k1 * r + a1);
			else if(r<r2)
				r = (int)(k2 * r + a2);
			else
				r = (int)(k3 * r + a3);
			
			if(g<r1)
				g = (int)(k1 * g + a1);
			else if(r<r2)
				g = (int)(k2 * g + a2);
			else
				g = (int)(k3 * g + a3);
			
			if(b<r1)
				b = (int)(k1 * b + a1);
			else if(r<r2)
				b = (int)(k2 * b + a2);
			else
				b = (int)(k3 * b + a3);
			
			
			if(r > 255) r = 255;
			if(g > 255) g = 255;
			if(b > 255) b = 255;
			
			gray = (int)((r + g + b) / 3);
			pixels[i] = 255 << 24 | gray << 16 | gray << 8 | gray;
			
			//pixels[i] = 255 << 24 | r << 16 | g << 8 | b;
		}
		
		return pixels;
	}
	
	public int [] getHistogram(int [] pixels, int w, int h)
	{
		int[] intensity = new int[256];
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b;
		int gray;
		
        for(int i=0; i<intensity.length; i++)
        {
        	intensity[i] = 0;
        }
        
        for(int i=0; i<w*h; i++)
		{
			r = cm.getRed(pixels[i]);
			g = cm.getGreen(pixels[i]);
			b = cm.getBlue(pixels[i]);
			gray = (int)((r + g + b) / 3);
			intensity[gray]++;
		}
        
        return intensity;
	}
	
	public int [] histogramEqualization(int [] pixels, int w, int h)
	{
		int length = w * h;
		int [] grayPixels = new int[length];
		int [] resultPixels = new int[length];
		int [] intensity = new int[256];
		int [] intensity2 = new int[256];
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b, gray;
		
		for(int i=0; i<w*h; i++)
		{
			r = cm.getRed(pixels[i]);
			g = cm.getGreen(pixels[i]);
			b = cm.getBlue(pixels[i]);
			gray = (int)((r + g + b) / 3);
			grayPixels[i] = gray;
		}
		
		intensity = getHistogram(pixels, w, h);
		intensity2[0]=intensity[0]; 
		for(int i=1 ;i<256 ;i++)
		{
			intensity2[i] = intensity2[i-1] + intensity[i]; 
		}
		for(int i=0; i<256; i++)
		{
			intensity2[i]=(int)(intensity2[i] * 255 / (w * h) + 0.5);
			//intensity2[i]=(int)(intensity2[i] * 255);
		}
		for(int i=0; i<h; i++) 
		{
			for(int j=0; j<w; j++) 
			{
				int k = i * w + j;
				resultPixels[k] = 0xFF000000 | ((intensity2[grayPixels[k]] << 16 ) | (intensity2[grayPixels[k]] << 8 ) | intensity2[grayPixels[k]]); 
			}
		}
		
		return resultPixels;
	}
	
	public BufferedImage showHistogram(int [] intensity)
	{
		int size = 280;
		BufferedImage histogramImage = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);

        // 绘制直方图
        // draw XY Axis lines
        Graphics2D g2d = histogramImage.createGraphics();
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, size, size);
        g2d.setPaint(Color.WHITE);
        g2d.drawLine(5, 250, 265, 250);
        g2d.drawLine(5, 250, 5, 5);
        
        // scale to 200
        g2d.setPaint(Color.GREEN);
        int max = -1;
		for(int i=0; i<intensity.length; i++) {
			if(max < intensity[i]) {
				max = intensity[i];
			}
		}
        float rate = 200.0f/((float)max);
        int offset = 2;
        for(int i=0; i<intensity.length; i++) {
        	int frequency = (int)(intensity[i] * rate);
        	g2d.drawLine(5 + offset + i, 250, 5 + offset + i, 250-frequency);
        }
        
        // X Axis Gray intensity
        g2d.setPaint(Color.RED);
        g2d.drawString("Gray Intensity", 100, 270);
		return histogramImage;
	}
	
	/**
     * 图像切割(按指定起点坐标和宽高切割)
     * @param srcImageFile 源图像地址
    * @param result 切片后的图像地址
    * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
    * @param height 目标切片高度
    */
	public BufferedImage cut(BufferedImage bi, int x, int y, int width, int height)
	{
		//try {
			// 读取源图像
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			//if (srcWidth > 0 && srcHeight > 0)
			//{
				Image image = bi.getScaledInstance(srcWidth, srcHeight,
						Image.SCALE_DEFAULT);
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
				Image img = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(image.getSource(),
								cropFilter));
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
				g.dispose();
				// 输出为文件
				//ImageIO.write(tag, "JPEG", new File(result));
				return tag;
			//}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public BufferedImage resize(BufferedImage source,int targetW,int targetH,boolean equalProportion){  
		int type=source.getType();  
		BufferedImage target=null;  
		double sx=(double)targetW/source.getWidth();  
		double sy=(double)targetH/source.getHeight();  
		//这里想实现在targetW，targetH范围内实现等比例的缩放  
		//如果不需要等比例的缩放则下面的if else语句注释调即可  
		if(equalProportion){  
			if(sx>sy){  
				sx=sy;  
				targetW=(int)(sx*source.getWidth());  
			}else{  
				sy=sx;  
				targetH=(int)(sx*source.getHeight());  
			}  
		}  
		if(type==BufferedImage.TYPE_CUSTOM){  
			ColorModel cm=source.getColorModel();  
			WritableRaster raster=cm.createCompatibleWritableRaster(targetW,targetH);  
			boolean alphaPremultiplied=cm.isAlphaPremultiplied();  
			target=new BufferedImage(cm,raster,alphaPremultiplied,null);  
		}else{  
			target=new BufferedImage(targetW,targetH,type);  
			Graphics2D g=target.createGraphics();  
			g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);  
			g.drawRenderedImage(source,AffineTransform.getScaleInstance(sx,sy));  
			g.dispose();  
		}  
		return target;  
	}

	public BufferedImage sobelEdgeImage(int [] pixels, int w, int h, int gradientThreshold) {

		float[] gradient = gradientM(w, h, pixels);// 计算图像各像素点的梯度值
		float maxGradient = gradient[0];
		BufferedImage outBinary = null;
		
		outBinary = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);// 生成边缘图像
		for (int i = 1; i < gradient.length; ++i)
			if (gradient[i] > maxGradient)
				maxGradient = gradient[i];// 获取梯度最大值
		float scaleFactor = 255.0f / maxGradient;// 比例因子用于调整梯度大小
		if (gradientThreshold >= 0) {
			for (int y = 1; y < h - 1; ++y)
				for (int x = 1; x < w - 1; ++x)
					if (Math.round(scaleFactor * gradient[y * w + x]) >= gradientThreshold)
						outBinary.setRGB(x, y, 0xffffff);// 白色
		}// 对梯度大小进行阈值处理
		else {
			for (int y = 1; y < h - 1; ++y)
				for (int x = 1; x < w - 1; ++x)
					outBinary.setRGB(x, y, 0x000000);// 黑色;
		}// //不对梯度大小进行阈值处理, 直接给出用比例因子调整后的值

		return outBinary;
	}

	//得到点(x,y)处的灰度值
	protected int getGrayPoint(int x, int y, int [] pixels, int w) {
		return pixels[y * w + x];
	}

	//算子计算 图像每个像素点 的 梯度大小
	protected float[] gradientM(int w, int h, int [] pixels) {
		int size;
		size = w * h;
		float[] mag = new float[size];

		int gx, gy;
		for (int y = 1; y < h - 1; ++y)
			for (int x = 1; x < w - 1; ++x) {
				gx = GradientX(x, y, pixels, w);
				gy = GradientY(x, y, pixels, w);
				//用公式 g=|gx|+|gy|计算图像每个像素点的梯度大小.原因是避免平方和开方耗费大量时间
				mag[y * w + x] = (float) (Math.abs(gx) + Math.abs(gy));
			}
		return mag;
	}

	//算子 计算 点(x,y)处的x方向梯度大小
	protected final int GradientX(int x, int y, int [] pixels, int w) {
		return getGrayPoint(x + 1, y - 1, pixels, w) + 2*getGrayPoint(x + 1, y, pixels, w)
				+ getGrayPoint(x + 1, y + 1, pixels, w) - getGrayPoint(x - 1, y - 1, pixels, w)
				- 2*getGrayPoint(x - 1, y, pixels, w) - getGrayPoint(x - 1, y + 1, pixels, w);
	}// 计算像素点(x,y)X方向上的梯度值

	// 算子 计算 点(x,y)处的y方向梯度大小
	protected final int GradientY(int x, int y, int [] pixels, int w) {
		return - getGrayPoint(x - 1, y - 1, pixels, w) - 2*getGrayPoint(x, y - 1, pixels, w)
				- getGrayPoint(x + 1, y - 1, pixels, w) + getGrayPoint(x - 1, y + 1, pixels, w)
				+ 2*getGrayPoint(x, y + 1, pixels, w) + getGrayPoint(x + 1, y + 1, pixels, w);
	}// 计算像素点(x,y)Y方向上的梯度值
	
	private final static float GAUSSIAN_CUT_OFF = 0.005f;
	private final static float MAGNITUDE_SCALE = 100F;
	private final static float MAGNITUDE_LIMIT = 1000F;
	private final static int MAGNITUDE_MAX = (int) (MAGNITUDE_SCALE * MAGNITUDE_LIMIT);
	
	public BufferedImage cannyEdgeImage(int [] pixels, int w, int h) {
		int picsize;
		int[] magnitude;
		BufferedImage edgesImage = null;
		
		float gaussianKernelRadius;
		float lowThreshold;
		float highThreshold;
		int gaussianKernelWidth;

		float[] xConv;
		float[] yConv;
		float[] xGradient;
		float[] yGradient;
		picsize = w * h;
		
		magnitude = new int[picsize];

		xConv = new float[picsize];
		yConv = new float[picsize];
		xGradient = new float[picsize];
		yGradient = new float[picsize];
		
		lowThreshold = 2.5f;
		highThreshold = 5f;
		gaussianKernelRadius = 2f;
		gaussianKernelWidth = 16;
		
		for (int i = 0; i < picsize; i++) {
			int p = pixels[i];
			int r = (p & 0xff0000) >> 16;
			int g = (p & 0xff00) >> 8;
			int b = p & 0xff;
			pixels[i] = (r + g + b) / 3;
		}
		
		magnitude = computeGradients(gaussianKernelRadius, gaussianKernelWidth, pixels, w, h, 
				xConv, yConv, magnitude, xGradient, yGradient);
		int low = Math.round(lowThreshold * MAGNITUDE_SCALE);
		int high = Math.round( highThreshold * MAGNITUDE_SCALE);
		magnitude = performHysteresis(low, high, pixels, w, h, magnitude);
		thresholdEdges(pixels, picsize);
		
		if (edgesImage == null) {
			edgesImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}
		edgesImage.getWritableTile(0, 0).setDataElements(0, 0, w, h, pixels);
		
		return edgesImage;
	}
	
	protected void thresholdEdges(int [] pixels, int picsize) {
		for (int i = 0; i < picsize; i++) {
			pixels[i] = pixels[i] > 0 ? -1 : 0xff000000;
		}
	}

	protected int [] computeGradients(float kernelRadius, int kernelWidth, int [] pixels, int w, int h, 
			float [] xConv, float [] yConv, int [] magnitude, float [] xGradient, float[] yGradient) {
		
		float kernel[] = new float[kernelWidth];
		float diffKernel[] = new float[kernelWidth];
		int kwidth;
		for (kwidth = 0; kwidth < kernelWidth; kwidth++) {
			float g1 = gaussian(kwidth, kernelRadius);
			if (g1 <= GAUSSIAN_CUT_OFF && kwidth >= 2) break;
			float g2 = gaussian(kwidth - 0.5f, kernelRadius);
			float g3 = gaussian(kwidth + 0.5f, kernelRadius);
			kernel[kwidth] = (g1 + g2 + g3) / 3f / (2f * (float) Math.PI * kernelRadius * kernelRadius);
			diffKernel[kwidth] = g3 - g2;
		}

		int initX = kwidth - 1;
		int maxX = w - (kwidth - 1);
		int initY = w * (kwidth - 1);
		int maxY = w * (h - (kwidth - 1));
		
		for (int x = initX; x < maxX; x++) {
			for (int y = initY; y < maxY; y += w) {
				int index = x + y;
				float sumX = pixels[index] * kernel[0];
				float sumY = sumX;
				int xOffset = 1;
				int yOffset = w;
				for(; xOffset < kwidth ;) {
					sumY += kernel[xOffset] * (pixels[index - yOffset] + pixels[index + yOffset]);
					sumX += kernel[xOffset] * (pixels[index - xOffset] + pixels[index + xOffset]);
					yOffset += w;
					xOffset++;
				}
				
				yConv[index] = sumY;
				xConv[index] = sumX;
			}
 
		}
 
		for (int x = initX; x < maxX; x++) {
			for (int y = initY; y < maxY; y += w) {
				float sum = 0f;
				int index = x + y;
				for (int i = 1; i < kwidth; i++)
					sum += diffKernel[i] * (yConv[index - i] - yConv[index + i]);
 
				xGradient[index] = sum;
			}
 
		}

		for (int x = kwidth; x < w - kwidth; x++) {
			for (int y = initY; y < maxY; y += w) {
				float sum = 0.0f;
				int index = x + y;
				int yOffset = w;
				for (int i = 1; i < kwidth; i++) {
					sum += diffKernel[i] * (xConv[index - yOffset] - xConv[index + yOffset]);
					yOffset += w;
				}
 
				yGradient[index] = sum;
			}
 
		}
 
		initX = kwidth;
		maxX = w - kwidth;
		initY = w * kwidth;
		maxY = w * (h - kwidth);
		for (int x = initX; x < maxX; x++) {
			for (int y = initY; y < maxY; y += w) {
				int index = x + y;
				int indexN = index - w;
				int indexS = index + w;
				int indexW = index - 1;
				int indexE = index + 1;
				int indexNW = indexN - 1;
				int indexNE = indexN + 1;
				int indexSW = indexS - 1;
				int indexSE = indexS + 1;
				
				float xGrad = xGradient[index];
				float yGrad = yGradient[index];
				float gradMag = hypot(xGrad, yGrad);

				float nMag = hypot(xGradient[indexN], yGradient[indexN]);
				float sMag = hypot(xGradient[indexS], yGradient[indexS]);
				float wMag = hypot(xGradient[indexW], yGradient[indexW]);
				float eMag = hypot(xGradient[indexE], yGradient[indexE]);
				float neMag = hypot(xGradient[indexNE], yGradient[indexNE]);
				float seMag = hypot(xGradient[indexSE], yGradient[indexSE]);
				float swMag = hypot(xGradient[indexSW], yGradient[indexSW]);
				float nwMag = hypot(xGradient[indexNW], yGradient[indexNW]);
				float tmp;
				
				if (xGrad * yGrad <= (float) 0 //(1)
					? Math.abs(xGrad) >= Math.abs(yGrad) //(2)
						? (tmp = Math.abs(xGrad * gradMag)) >= Math.abs(yGrad * neMag - (xGrad + yGrad) * eMag) //(3)
							&& tmp > Math.abs(yGrad * swMag - (xGrad + yGrad) * wMag) //(4)
						: (tmp = Math.abs(yGrad * gradMag)) >= Math.abs(xGrad * neMag - (yGrad + xGrad) * nMag) //(3)
							&& tmp > Math.abs(xGrad * swMag - (yGrad + xGrad) * sMag) //(4)
					: Math.abs(xGrad) >= Math.abs(yGrad) //(2)
						? (tmp = Math.abs(xGrad * gradMag)) >= Math.abs(yGrad * seMag + (xGrad - yGrad) * eMag) //(3)
							&& tmp > Math.abs(yGrad * nwMag + (xGrad - yGrad) * wMag) //(4)
						: (tmp = Math.abs(yGrad * gradMag)) >= Math.abs(xGrad * seMag + (yGrad - xGrad) * sMag) //(3)
							&& tmp > Math.abs(xGrad * nwMag + (yGrad - xGrad) * nMag) //(4)
					) {
					magnitude[index] = gradMag >= MAGNITUDE_LIMIT ? MAGNITUDE_MAX : (int) (MAGNITUDE_SCALE * gradMag);
				} else {
					magnitude[index] = 0;
				}
			}
		}
		return magnitude;
	}
	
	private float hypot(float x, float y) {
		return (float) Math.hypot(x, y);
	}
	
	protected float gaussian(float x, float sigma) {
		return (float) Math.exp(-(x * x) / (2f * sigma * sigma));
	}
	
	protected int [] performHysteresis(int low, int high, int [] pixels, int w, int h, int [] magnitude) {
		Arrays.fill(pixels, 0);
 
		int offset = 0;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (pixels[offset] == 0 && magnitude[offset] >= high) {
					magnitude = follow(x, y, offset, low, pixels, w, h, magnitude);
				}
				offset++;
			}
		}
		return magnitude;
 	}
	
	protected int [] follow(int x1, int y1, int i1, int threshold, int [] pixels, int w, int h, int [] magnitude) {
		int x0 = x1 == 0 ? x1 : x1 - 1;
		int x2 = x1 == w - 1 ? x1 : x1 + 1;
		int y0 = y1 == 0 ? y1 : y1 - 1;
		int y2 = y1 == h -1 ? y1 : y1 + 1;
		
		pixels[i1] = magnitude[i1];
		for (int x = x0; x <= x2; x++) {
			for (int y = y0; y <= y2; y++) {
				int i2 = x + y * w;
				if ((y != y1 || x != x1)
					&& pixels[i2] == 0 
					&& magnitude[i2] >= threshold) {
					magnitude = follow(x, y, i2, threshold, pixels, w, h, magnitude);
					return magnitude;
				}
			}
		}
		return magnitude;
	}
	
	public int [] addMosaic(int [] pixels, int w, int h, int windowSize)
	{
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b, mr, mg, mb;
		int countx, county;
		countx = w / windowSize;
		county = h / windowSize;
		
		for(int i=0; i<countx*county; i++)
		{
			int n = (int)(i / countx);
			int m = (int)(i % countx);
			int start = n * (windowSize * w) + m * windowSize;
			mr = cm.getRed(pixels[start]);
			mg = cm.getGreen(pixels[start]);
			mb = cm.getBlue(pixels[start]);
			for(int j=0; j<windowSize; j++)
			{
				for(int k=0; k<windowSize; k++)
				{
					int idx = start + (j * w +k);
					r = cm.getRed(pixels[idx]) & 0xF0;
					g = cm.getGreen(pixels[idx]) & 0xF0;
					b = cm.getBlue(pixels[idx]) & 0xF0;
					r = r >> 4;
					g = g >> 4;
					b = b >> 4;
					r = (mr & 0xF0) | r;
					g = (mg & 0xF0) | g;
					b = (mb & 0xF0) | b;
//					System.out.println(Integer.toHexString(r << 4));
//					pixels[idx] = 255 << 24 | r << 20 | g << 12 | b << 4;
					pixels[idx] = 255 << 24 | r << 16 | g << 8 | b;
				}
			}
		}
		return pixels;
	}
	
	public int [] removeMosaic(int [] pixels, int w, int h, int windowSize)
	{
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b;
		int countx, county;
		countx = w / windowSize;
		county = h / windowSize;
		
		for(int i=0; i<countx*county; i++)
		{
			int n = (int)(i / countx);
			int m = (int)(i % countx);
			int start = n * (windowSize * w) + m * windowSize;
			for(int j=0; j<windowSize; j++)
			{
				for(int k=0; k<windowSize; k++)
				{
					int idx = start + (j * w +k);
					r = cm.getRed(pixels[idx]) & 0x0F;
					g = cm.getGreen(pixels[idx]) & 0x0F;
					b = cm.getBlue(pixels[idx]) & 0x0F;
//					System.out.println(Integer.toHexString(r << 4));
					pixels[idx] = 255 << 24 | r << 20 | g << 12 | b << 4;
				}
			}
		}
		
		return pixels;
	}
}
