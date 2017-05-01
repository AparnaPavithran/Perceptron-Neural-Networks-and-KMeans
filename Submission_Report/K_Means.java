
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;


import javax.imageio.ImageIO;
 

public class K_Means {
    public static void main(String [] args){
		if (args.length < 4){
			System.out.println("Input string missing");
		    System.out.println("Usage: Kmeans <input-image> <k> <output-image> <iterations>");
		    return;
		}
		try{
			
		    BufferedImage originalImage = ImageIO.read(new File(args[0]));
		    int k=Integer.parseInt(args[1]);
		    int iterations=Integer.parseInt(args[3]);
		    
		    BufferedImage kmeansJpg = kmeans_helper(originalImage,k,iterations);
		    ImageIO.write(kmeansJpg, "jpg", new File(args[2])); 
		    
		    File imageFile_in = new File(args[2]);
			File imageFile_out = new File(args[0]);
		    double img_len_aft = imageFile_in.length();
			double img_len_bef = imageFile_out.length();
			//System.out.println("img len after"+img_len_aft);
			double compression_ratio = (img_len_aft/img_len_bef)*100;
			System.out.println("Compression ratio : "+compression_ratio);
		}catch(IOException e){
		    System.out.println(e.getMessage());
		}	
    }
    
    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k,int iterations)
    {
    	System.out.println("kmeans helper");	
		int w=originalImage.getWidth();
		int h=originalImage.getHeight();
		BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
		Graphics2D g = kmeansImage.createGraphics();
		g.drawImage(originalImage, 0, 0, w,h , null);
		// Read rgb values from the image
		int[] rgb=new int[w*h];
		int count=0;
		for(int i=0;i<w;i++){
		    for(int j=0;j<h;j++){
			rgb[count++]=kmeansImage.getRGB(i,j);
		    }
		}
		// Call kmeans algorithm: update the rgb values
		rgb=kmeans(rgb,k,iterations);

		// Write the new rgb values to the image
		count=0;
		for(int i=0;i<w;i++){
		    for(int j=0;j<h;j++){
			kmeansImage.setRGB(i,j,rgb[count++]);
		    }
		}
		return kmeansImage;
    }

    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static int[] kmeans(int[] rgb, int k,int iterations){
    	System.out.println("kmeans");
    	int[] cluster_center = new int[k];
    	int[] cluster_center_init =new int[k];
    	int[] classified_clustor =new int[rgb.length];
    	int[] count= new int[k];
    	int[] count_flag = new int[k];
    	
    	double[] mean_vector = new double[k];
    	double[] prev_mean = new double[k];
    	
    	int i=0,j=0,classified=0,p=0;
    	Random r= new Random();
    	for(i=0;i<k;i++)
    	{
    		cluster_center[i]=r.nextInt(rgb.length);
    		cluster_center_init[i]=cluster_center[i];
    		//System.out.println("cluster_center_init :"+cluster_center_init[i]);
    		mean_vector[i] =0.0;
    		prev_mean[i]=-0.1;
    		count[i]=0;
    		count_flag[i]=0;
    		
    	}
    	
    	
    	for(i=0;classified<k;i++) 
    	{
    		//System.out.println("kmeans loop "+i);
    		
    		if(i>iterations)
    		{
    			break;
    		}
    		// when all converges come out
    		for(j=0;j<rgb.length;j++)
    		{
    			double[] distance_vector = new double[k];
    			Color color1=new Color(rgb[j]);
    			for(p=0;p<k;p++)
    			{
    				Color color2;
    				if(i==0)
    				{
    					int col=cluster_center[p];
    					color2= new Color(rgb[col]);
    				}
    				else
    				{
    					color2= new Color(cluster_center[p]);//for first time if i=0, change to rgb of cluster center 
    				}
    				
    				if(prev_mean[p]!=0.070707)
    				{
    					//update distance vector for each cluster
    					double red_dist,green_dist,blue_dist;
    					red_dist = Math.abs(color1.getRed() - color2.getRed());
    					green_dist = Math.abs(color1.getGreen() - color2.getGreen());
    					blue_dist = Math.abs(color1.getBlue() - color2.getBlue());
    					distance_vector[p]= Math.sqrt(red_dist*red_dist + green_dist*green_dist + blue_dist*blue_dist);
    					
    				}
    			}
    			//find min of distance vector assign to classified_clustor
				double min_dist=distance_vector[0];
				int flag=0;
				for(p=1;p<k;p++)
				{
					double dist=distance_vector[p];
					if(dist<min_dist)
					{
						min_dist=dist;
						flag=p;
					}
				}
				classified_clustor[j]=flag;
				//find mean 
				mean_vector[flag]=mean_vector[flag]+rgb[j];
				count[flag]=count[flag]+1;
				//compare mean n prev mean
				
    			
    		}
    		
    		for(j=0;j<k;j++)
    		{
    			mean_vector[j]=mean_vector[j]/count[j];
    			cluster_center[j]=(int) mean_vector[j];
    			//System.out.println("Current mean :"+mean_vector[j]+"cluster center : "+cluster_center[j]);
    			if(mean_vector[j]==prev_mean[j] ) 
    			{
    				
    				
    				if(count_flag[j]==0)
    				{
    					count_flag[j]=1;
    					classified++;
    					//System.out.println("Classified :"+classified);
    					//System.out.println("Previous mean :"+prev_mean[j]+"Current mean :"+mean_vector[j]);
    				}
    				else
    				{
    					//System.out.println("Classified :"+classified);
    				}
    				
    			}
    			else
    			{
    				prev_mean[j]=mean_vector[j];
            		mean_vector[j] =0.0;
    			}
    		}
    		
    			
    		
    		//find no of cluster points from classified_cluster n update cluster_center
    		
    	}
    	for(i=0;i<rgb.length;i++)
    	{
    		p=classified_clustor[i];
    		j=cluster_center_init[p];
    		rgb[i]=rgb[j];
    		//System.out.println("cluster_center_init :"+p);
    	}
    	return rgb;
    	
    }

}
