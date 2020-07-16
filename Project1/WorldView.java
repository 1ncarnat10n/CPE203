import processing.core.PApplet;

import java.util.Scanner;

/*
WorldView ideally mostly controls drawing the current part of the whole world
that we can see based on the viewport
*/

final class WorldView
{
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;
   private Viewport viewport;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

   public void drawEntities()
   {
      for (Entity entity : this.world.getEntities())
      {
         Point pos = entity.getPosition();

         if (this.viewport.contains(pos))
         {
            Point viewPoint = this.viewport.worldToViewport(pos.getX(), pos.getY());
            this.screen.image(entity.getCurrentImage(entity),
                    viewPoint.getX() * this.tileWidth, viewPoint.getY() * this.tileHeight);
         }
      }
   }

   public void shiftView(int colDelta, int rowDelta)
   {
      int newCol = Functions.clamp(this.viewport.getCol() + colDelta, 0,
              this.world.getNumCols() - this.viewport.getNumCol());
      int newRow = Functions.clamp(this.viewport.getRow() + rowDelta, 0,
              this.world.getNumRows() - this.viewport.getNumRow());

      this.viewport.shift(newCol, newRow);
   }

   public Viewport getViewport() {
      return viewport;
   }

   public int getTileHeight() {
      return tileHeight;
   }

   public int getTileWidth() {
      return tileWidth;
   }

   public PApplet getScreen() {
      return screen;
   }

   public WorldModel getWorld() {
      return world;
   }
}
