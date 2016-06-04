/**
 * 离散余弦变换
 * @author cv
 *
 */
public class DCT2 {
	double[][] dct_image;                 //DCT image;
    double[][] dct_coef;                  //DCT系数矩阵
    double[][] dct_coeft;                 //转置DCT系数矩阵
    double[][] image;                     //bsize * bsize图像块

    /*------------------------------------------------------------*
     * in_image  输入图像矩阵
     * iw, ih    输入图像宽高
     * bsize     bsizeXbsize图像块DCT变换
     *------------------------------------------------------------*/
    public double[][] dctTrans(double[][] img, int iw, int ih, int bsize)
    {
        int iter_num = iw / bsize;
        dct_image = new double[iw][ih];
        dct_coef  = new double[bsize][bsize];
        dct_coeft = new double[bsize][bsize];
        image = new double[bsize][bsize];
        
        coeff(dct_coef, bsize);
        
        //定义转置矩阵系数
        for (int i = 0; i < bsize; i++)
            for (int j = 0; j < bsize; j++)
                dct_coeft[i][j] = dct_coef[j][i];
     	
        for (int j = 0; j < iter_num; j++)
        {
            for (int i = 0; i < iter_num; i++)
            {
                //取bsizeXbsize图像块image[][]
                for (int k = 0; k < bsize; k++)
                    for (int l = 0; l < bsize; l++)                            
                        image[k][l] = img[i * bsize + k][j * bsize + l];
                 
                //bsize * bsize块DCT
                dct(image, dct_coeft, dct_coef, bsize);
                
                //输出DCT图像
                for (int k = 0; k < bsize; k++)
                    for (int l = 0; l < bsize; l++)
                        dct_image[i * bsize + k][j * bsize + l] = image[k][l];
            }
        }
        return dct_image;
    }

    public void coeff(double[][] dct_coef, int n)
    {
        double sqrt_1 = 1.0 / Math.sqrt(2.0);

        for (int i = 0; i < n; i++)
            dct_coef[0][i] = sqrt_1;

        //初始化DCT系数
        for (int i = 1; i < n; i++)
            for (int j = 0; j < n; j++)
                dct_coef[i][j] = Math.cos(i * Math.PI * (j + 0.5) / ((double)n));
    }

    public void dct(double[][] a, double[][] b, double[][] c, int n)
    {
        double x;
        double[][] af = new double[n][n];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                x = 0.0;
                for (int k = 0; k < n; k++)
                    x += a[i][k] * b[k][j];
                af[i][j] = x;
            }
        }
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                x = 0.0;
                for (int k = 0; k < n; k++)
                    x += c[i][k] * af[k][j];
                a[i][j] = 2.0 * x / ((double)n);
            }
        }
    }
    public int[] toPixels(double[][] inIm, int iw, int ih)
    {
    	int r;
        int[] pix = new int[iw*ih];
        for (int j = 0; j < ih; j++)
        {
            for (int i = 0; i < iw; i++)
            {
                r = (int)inIm[i][j];
                if (r < 0) r = 0;
                else if (r > 255) r = 255;
                pix[i + j * iw] = 255 << 24|r << 16|r << 8|r;                
            }
        }
        return pix;
    }
}
