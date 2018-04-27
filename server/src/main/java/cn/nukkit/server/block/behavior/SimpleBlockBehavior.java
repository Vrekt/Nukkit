package cn.nukkit.server.block.behavior;

import cn.nukkit.api.Player;
import cn.nukkit.api.block.Block;
import cn.nukkit.api.item.ItemInstance;
import cn.nukkit.api.item.ItemInstanceBuilder;
import cn.nukkit.server.item.behavior.ItemBehaviorUtil;
import cn.nukkit.server.network.minecraft.session.PlayerSession;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.Collection;

public class SimpleBlockBehavior implements BlockBehavior {
    public static final SimpleBlockBehavior INSTANCE = new SimpleBlockBehavior();

    @Override
    public Collection<ItemInstance> getDrops(Player player, Block block, @Nullable ItemInstance item) {
        if (!block.getBlockState().getBlockType().isDiggable()) {
            return ImmutableList.of();
        }
        // TODO: Fortune drops
        ItemInstanceBuilder builder = player.getServer().itemInstanceBuilder()
                .itemType(block.getBlockState().getBlockType())
                .amount(1);

        block.getBlockState().getBlockData().ifPresent(builder::itemData);

        return ImmutableList.of(builder.build());
    }

    @Override
    public float getBreakTime(Player player, Block block, @Nullable ItemInstance item) {
        float breakTime = block.getBlockState().getBlockType().hardness();

        if (isCorrectTool(item)) {
            breakTime *= 1.5;
            breakTime /= ItemBehaviorUtil.getMiningEfficiency(item);
        } else {
            breakTime *= 5;
        }

        return breakTime;
    }

    @Override
    public boolean isCorrectTool(@Nullable ItemInstance item) {
        return true;
    }

    @Override
    public Result onBreak(PlayerSession session, Block block, ItemInstance withItem) {
        return Result.BREAK_BLOCK;
    }

    @Override
    public boolean onPlace(PlayerSession session, Block against, ItemInstance withItem) {
        return false;
    }
}