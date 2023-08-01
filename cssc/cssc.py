import copy

# views ideals as a 2D array

start = [[8, 8, 8, 8, 7, 6, 5, 4],
         [8, 8, 8, 7, 6, 5, 4, 3],
         [8, 8, 7, 6, 5, 4, 3, 2],
         [8, 7, 6, 5, 4, 3, 2, 1],
         [7, 6, 5, 4, 3, 2, 1, 0],
         [6, 5, 4, 3, 2, 1, 0, 0],
         [5, 4, 3, 2, 1, 0, 0, 0],
         [4, 3, 2, 1, 0, 0, 0, 0]]

# start = [[6, 6, 6, 5, 4, 3], [6, 6, 5, 4, 3, 2], [6, 5, 4, 3, 2, 1], [5, 4, 3, 2, 1, 0], [4, 3, 2, 1, 0, 0], [3, 2, 1, 0, 0, 0]]

# start = [[4, 4, 3, 2],
#          [4, 3, 2, 1],
#          [3, 2, 1, 0],
#          [2, 1, 0, 0]]

n = len(start)

# either k is one more than when we 0 index, or we will have to change the start ideal

def check_potential_peak(ideal, i, j):
    return (j == n-1 or ideal[i][j] > ideal[i][j+1]) and (i == n-1 or ideal[i][j] > ideal[i+1][j]) and ideal[i][j] > 0

def get_potential_peaks(ideal):
    potential_peaks = set()
    for i in range(n):
        for j in range(n):
            if check_potential_peak(ideal, i, j):
                potential_peaks.add((i, j))
    return potential_peaks

def check_ideal(ideal):
    for i in range(n):
        for j in range(n-1):
            if ideal[i][j] < ideal[i][j+1]:
                return False
    for i in range(n-1):
        for j in range(n):
            if ideal[i][j] < ideal[i+1][j]:
                return False
    return True

def flip(ideal, coords):
    """
    ideal is a 2D array, coords is (i, j) of the point to be flipped
    """
    (i, j) = coords
    k = ideal[i][j] - 1 # need ideal[i][j] >= 1 to be able to flip
    new_ideal = copy.deepcopy(ideal)
    
    new_ideal[i][j] -= 1
    new_ideal[j][k] -= 1
    new_ideal[k][i] -= 1
    new_ideal[n - 1 - i][n - 1 - j] += 1
    new_ideal[n - 1 - j][n - 1 - k] += 1
    new_ideal[n - 1 - k][n - 1 - i] += 1
    
    if check_ideal(new_ideal) and not ((i+j == n-1 and j+k == n-1) or (j+k == n-1 and k+i == n-1) or (k+i == n-1 and i+j == n-1)):
        return new_ideal
    return ideal
    
def all_ideals():
    visited = [start]
    to_do = [start]

    while to_do:
        ideal = to_do.pop()
        
        for peak in get_potential_peaks(ideal):
            new_ideal = flip(ideal, peak)
            if check_ideal(new_ideal) and new_ideal not in visited:
                # print("original ideal", ideal, "peak", peak, "new ideal", new_ideal)
                to_do.append(new_ideal)
                visited.append(new_ideal)
                
    return visited

def num_ideals():
    # there is a formula but since there are so few cases I will just write out the numbers
    if n == 4:
        return 2**2
    elif n == 6:
        return 7**2
    elif n == 8:
        return 42**2  
    elif n == 10:
        return 429**2 
    elif n == 12:
        return 7436**2
    raise KeyError

# for ideal in all_ideals():
#     print(ideal)
    
big_list = all_ideals()

eccentricities = []

# n=8

farthest_from_center = []

for i in range(num_ideals()):
    max_sum = 0
    for j in range(num_ideals()):
        the_sum = 0
        if i != j:
            for a in range(n):
                for b in range(n):
                    the_sum += abs(big_list[i][a][b] - big_list[j][a][b])
            if the_sum > max_sum:
                max_sum = the_sum
    if max_sum == 120:
        print(big_list[i])
    # eccentricities.append((i, max_sum / 6))

# print(farthest_from_center)

# specific to n=6
# perimeter = []
# farthest_from_center = []

# for i in range(0, 1):
#     # max_sum = 0
#     for j in range(49):
#         sum = 0
#         if i != j:
#             for a in range(n):
#                 for b in range(n):
#                     sum += abs(big_list[i][a][b] - big_list[j][a][b])
#             # if sum > max_sum:
#             #     max_sum = sum
#             if sum == 24:
#                 print(big_list[j])
    # if max_sum == 48:
    #     perimeter.append(big_list[i])
    # eccentricities.append((i, max_sum / 6))
            
# print(eccentricities)
# print(perimeter)
# for ideal in perimeter:
    # print(ideal[3][3:], ideal[4][3:], ideal[5][3:])

# diameter is the max distance out of these (/ 6 technically)
# want to find max distance for any given vertex, or maybe average distance??