package fr.ironcraft.quakecraft.v1_5_R3;

import org.bukkit.util.Vector;

import net.minecraft.server.v1_5_R3.*;

public class EntityFireworksNew extends EntityFireworks {

    private int ticksFlown;
    public int expectedLifespan; // CraftBukkit - private -> public

    public EntityFireworksNew(World world) {
        super(world);
        this.a(0.25F, 0.25F);
    }
    @Override
    protected void a() {
        this.datawatcher.a(8, 5);
    }

    public EntityFireworksNew(World world, double d0, double d1, double d2) {
        super(world);
        this.ticksFlown = 0;
        this.a(0.25F, 0.25F);
        this.setPosition(d0, d1, d2);
        this.height = 0.0F;
        int i = 1;


        this.motX = this.random.nextGaussian() * 0.001D;
        this.motZ = this.random.nextGaussian() * 0.001D;
        this.motY = 0.05D;
        this.expectedLifespan = 10 * i + this.random.nextInt(6) + this.random.nextInt(7);
    }
    @Override
    public void l_() {
        this.U = this.locX;
        this.V = this.locY;
        this.W = this.locZ;
       this.move(this.motX, this.motY, this.motZ);
        float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);

        this.yaw = (float) (Math.atan2(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);

        for (this.pitch = (float) (Math.atan2(this.motY, (double) f) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
            ;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        this.pitch = this.lastPitch + (this.pitch - this.lastPitch) * 0.2F;
        this.yaw = this.lastYaw + (this.yaw - this.lastYaw) * 0.2F;
        if (this.ticksFlown == 0) {
        	this.world.makeSound(this, "mob.creeper.say", 0.6F, 3.0F);
        }
        this.world.broadcastEntityEffect(this, (byte) 17);
        ++this.ticksFlown;
        if (this.world.isStatic) {
            this.world.addParticle("fireworksSpark", this.locX, this.locY - 0.3D, this.locZ, this.random.nextGaussian() * 0.05D, -this.motY * 0.5D, this.random.nextGaussian() * 0.05D);
        }

        if (!this.world.isStatic && this.ticksFlown > this.expectedLifespan) {
            this.world.broadcastEntityEffect(this, (byte) 17);
            this.die();
        }
        
    }
    @Override
    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInt("Life", this.ticksFlown);
        nbttagcompound.setInt("LifeTime", this.expectedLifespan);
        ItemStack itemstack = this.datawatcher.getItemStack(8);

        if (itemstack != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            itemstack.save(nbttagcompound1);
            nbttagcompound.setCompound("FireworksItem", nbttagcompound1);
        }
    }
    @Override
    public void a(NBTTagCompound nbttagcompound) {
        this.ticksFlown = nbttagcompound.getInt("Life");
        this.expectedLifespan = nbttagcompound.getInt("LifeTime");
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("FireworksItem");

        if (nbttagcompound1 != null) {
            ItemStack itemstack = ItemStack.createStack(nbttagcompound1);

            if (itemstack != null) {
                this.datawatcher.watch(8, itemstack);
            }
        }
    }
    @Override
    public float c(float f) {
        return super.c(f);
    }
    @Override
    public boolean ap() {
        return false;
    }

    public void setVelocity(Vector vel) {
        this.motX = vel.getX();
        this.motY = vel.getY();
        this.motZ = vel.getZ();
        this.velocityChanged = true;
    }
}
