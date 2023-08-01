# center = [[6, 6, 6, 5, 4, 3], [6, 6, 5, 4, 3, 2], [6, 5, 4, 3, 2, 1], [5, 4, 3, 2, 1, 0], [4, 3, 2, 1, 0, 0], [3, 2, 1, 0, 0, 0]]

# testing = [[6, 6, 5, 5, 5, 4], [6, 5, 5, 3, 3, 1], [6, 5, 5, 3, 3, 0], [6, 3, 3, 1, 1, 0], [5, 3, 3, 1, 1, 0], [2, 1, 1, 1, 0, 0]]

# the_sum = 0
# for i in range(6):
#     for j in range(6):
#         the_sum += abs(center[i][j] - testing[i][j])
        
# print(the_sum)
        
# n = 10
# blank_array = [[0] * n for _ in range(n)]

# blank_array[1][5] = 2
# print(blank_array)

# potential_perimeter_1 = [[8, 8, 8, 8, 7, 7, 7, 4], [8, 7, 7, 7, 6, 6, 4, 1], [8, 7, 6, 6, 5, 4, 2, 1], [8, 7, 6, 5, 4, 3, 2, 1], [7, 6, 5, 4, 3, 2, 1, 0], [7, 6, 4, 3, 2, 2, 1, 0], [7, 4, 2, 2, 1, 1, 1, 0], [4, 1, 1, 1, 0, 0, 0, 0]]
# potential_perimeter_2 = [[8, 8, 8, 8, 4, 4, 4, 4], [8, 8, 8, 8, 4, 4, 4, 4], [8, 8, 8, 8, 4, 4, 4, 4], [8, 8, 8, 8, 4, 4, 4, 4], [4, 4, 4, 4, 0, 0, 0, 0], [4, 4, 4, 4, 0, 0, 0, 0], [4, 4, 4, 4, 0, 0, 0, 0], [4, 4, 4, 4, 0, 0, 0, 0]]

# the_sum = 0
# for i in range(8):
#     for j in range(8):
#         the_sum += abs(potential_perimeter_1[i][j] - potential_perimeter_2[i][j])
        
# print(the_sum) # seems to achieve the diameter bound!

array = [[4, 3, 2, 5], [4, 3, 2, 5], [4, 3, 2, 5], [4, 3, 2, 5]]
for item in array[2:][2:]:
    print(item)