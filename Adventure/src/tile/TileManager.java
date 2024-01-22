package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
	
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	
	public TileManager(GamePanel gp) throws IOException {
		this.gp = gp;
		
		tile = new Tile[10];
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		
        getTileImage();
		loadMap("/maps/world01.txt");
	}
	
	public void getTileImage() {
	    String[] tilePaths = {"/tiles/grass.png", "/tiles/wall.png", "/tiles/water.png", "/tiles/earth.png", "/tiles/tree.png", "/tiles/sand.png"};

	    for (int i = 0; i < tilePaths.length; i++) {
	        try (InputStream input = getClass().getResourceAsStream(tilePaths[i])) {
	            if (input == null) {
	                throw new IOException("Failed to load tile image: " + tilePaths[i]);
	            }

	            BufferedImage image = ImageIO.read(input);

	            if (image == null) {
	                throw new IOException("Failed to read image from: " + tilePaths[i]);
	            }

	            tile[i] = new Tile();
	            tile[i].image = image;

	            // Se for um tile com colisão, marque-o
	            if (i == 1 || i == 2 || i == 4) {
	                tile[i].collision = true;
	            }
	        } catch (IOException e) {
	            System.err.println("Error loading tile image: " + tilePaths[i]);
	            e.printStackTrace();
	        }
	    }
	}
	public void loadMap(String filePath) {
		try{
			
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				
				String line = br.readLine();
				
				while(col < gp.maxWorldCol) {
					String numbers[] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[col][row] = num ;
					col++;
				}
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void draw (Graphics2D g2) {
		
//		g2.drawImage(tile[0].image, 0, 0,gp.tileSize, gp.tileSize, null);
//		g2.drawImage(tile[1].image, 48, 0,gp.tileSize, gp.tileSize, null);
//		g2.drawImage(tile[2].image, 96, 0,gp.tileSize, gp.tileSize, null);
		int worldCol = 0;
		int worldRow = 0;
		
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){
			
			int tileNum = mapTileNum[worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.getWorldX() + gp.player.screenX; 
			int screenY = worldY - gp.player.getWorldY() + gp.player.screenY; 
			
			if(worldX + gp.tileSize > gp.player.getWorldX() - gp.player.screenX &&
			   worldX - gp.tileSize < gp.player.getWorldX() + gp.player.screenX &&
			   worldY + gp.tileSize  > gp.player.getWorldY() - gp.player.screenY &&
			   worldY - gp.tileSize < gp.player.getWorldY() + gp.player.screenY) {
				
				g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			}
			
			worldCol++;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol =0;
				worldRow++;
			}
			
		}
	}
}
