Stream.of(
Block.makeCuboidShape(0, 3, 0, 16, 16, 2),
Block.makeCuboidShape(1, 13, -0.5, 15, 14, 0),
Block.makeCuboidShape(1, 13, 16, 15, 14, 16.5),
Block.makeCuboidShape(-0.5, 13, 1, 0, 14, 15),
Block.makeCuboidShape(16, 13, 1, 16.5, 14, 15),
Block.makeCuboidShape(-0.5, 11, 1, 0, 12, 15),
Block.makeCuboidShape(1, 11, 16, 15, 12, 16.5),
Block.makeCuboidShape(16, 11, 1, 16.5, 12, 15),
Block.makeCuboidShape(1, 11, -0.5, 15, 12, 0),
Block.makeCuboidShape(-1.25, 15.5, -1.25, 1.75, 16.5, 1.75),
Block.makeCuboidShape(-0.75, 16.5, 1.25, 1.25, 17.5, 1.75),
Block.makeCuboidShape(-1.25, 16.5, -1.25, -0.75, 17.5, 1.75),
Block.makeCuboidShape(1.25, 16.5, -1.25, 1.75, 17.5, 1.75),
Block.makeCuboidShape(-0.75, 16.5, -1.25, 1.25, 17.5, -0.75),
Block.makeCuboidShape(0, 3, 14, 16, 16, 16),
Block.makeCuboidShape(14, 3, 2, 16, 16, 14),
Block.makeCuboidShape(1, 2, 1, 15, 2.5, 15),
Block.makeCuboidShape(0.5, 1.5, 0.5, 15.5, 2, 15.5),
Block.makeCuboidShape(0.5, 2.5, 0.5, 15.5, 3, 15.5),
Block.makeCuboidShape(-0.25, 2.6, -0.25, 1.75, 3.5, 1.75),
Block.makeCuboidShape(-0.75, 3.5, -0.75, 1.25, 15.5, 1.25),
Block.makeCuboidShape(0.25, 0.5, 0.25, 3.25, 2.75, 3.25),
Block.makeCuboidShape(-0.25, 0, -0.25, 3.75, 0.5, 3.75),
Block.makeCuboidShape(12.75, 0.5, 0.25, 15.75, 2.75, 3.25),
Block.makeCuboidShape(12.25, 0, -0.25, 16.25, 0.5, 3.75),
Block.makeCuboidShape(12.75, 0.5, 12.75, 15.75, 2.75, 15.75),
Block.makeCuboidShape(12.25, 0, 12.25, 16.25, 0.5, 16.25),
Block.makeCuboidShape(0.25, 0.5, 12.75, 3.25, 2.75, 15.75),
Block.makeCuboidShape(-0.25, 0, 12.25, 3.75, 0.5, 16.25),
Block.makeCuboidShape(14.25, 2.6, -0.25, 16.25, 3.5, 1.75),
Block.makeCuboidShape(14.75, 3.5, -0.75, 16.75, 15.5, 1.25),
Block.makeCuboidShape(14.25, 15.5, -1.25, 17.25, 16.5, 1.75),
Block.makeCuboidShape(14.25, 16.5, -1.25, 14.75, 17.5, 1.75),
Block.makeCuboidShape(14.75, 16.5, -1.25, 16.75, 17.5, -0.75),
Block.makeCuboidShape(16.75, 16.5, -1.25, 17.25, 17.5, 1.75),
Block.makeCuboidShape(14.75, 16.5, 1.25, 16.75, 17.5, 1.75),
Block.makeCuboidShape(14.25, 2.6, 14.25, 16.25, 3.5, 16.25),
Block.makeCuboidShape(14.75, 3.5, 14.75, 16.75, 15.5, 16.75),
Block.makeCuboidShape(14.25, 15.5, 14.25, 17.25, 16.5, 17.25),
Block.makeCuboidShape(14.25, 16.5, 14.25, 14.75, 17.5, 17.25),
Block.makeCuboidShape(14.75, 16.5, 16.75, 16.75, 17.5, 17.25),
Block.makeCuboidShape(16.75, 16.5, 14.25, 17.25, 17.5, 17.25),
Block.makeCuboidShape(14.75, 16.5, 14.25, 16.75, 17.5, 14.75),
Block.makeCuboidShape(-0.25, 2.6, 14.25, 1.75, 3.5, 16.25),
Block.makeCuboidShape(-0.75, 3.5, 14.75, 1.25, 15.5, 16.75),
Block.makeCuboidShape(-1.25, 15.5, 14.25, 1.75, 16.5, 17.25),
Block.makeCuboidShape(-0.75, 16.5, 16.75, 1.25, 17.5, 17.25),
Block.makeCuboidShape(-1.25, 16.5, 14.25, -0.75, 17.5, 17.25),
Block.makeCuboidShape(1.25, 16.5, 14.25, 1.75, 17.5, 17.25),
Block.makeCuboidShape(-0.75, 16.5, 14.25, 1.25, 17.5, 14.75),
Block.makeCuboidShape(0, 3, 2, 2, 16, 14)
).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();