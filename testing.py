# for a in range(1, 10):
#     for b in range(a, 10):
#         for c in range(b, 10):
#             if a*b*c % 2 == 0:
#                 print("dimensions:", a, "x", b, "x", c, ", number of vertices:", len(all_ideals(a, b, c)))
#                 # print(all_ideals(a, b, c))

from math import comb, floor, ceil

a = 2

for b in range(2, 10):
    for c in range(b, 10):
        print("dimensions:", a, "x", b, "x", c, ", number of vertices:", comb(floor(b/2) + ceil(c/2), floor(b/2)) * comb(ceil(b/2) + floor(c/2), floor(c/2)))
        # print("dimensions:", a, "x", b, "x", c, ", number of vertices:", comb(floor((b+c)//2), floor(b//2)) * comb(ceil((b+c)//2), floor(c//2)))