import numpy as np

n = 5

# points are tuples of length n 
# ideals are represented by a set of their peaks (tuples)

def get_children(point):
    """
    returns a set of the points directly covered by the given point in the boolean poset
    """
    children = set()
    for i in range(n):
        if point[i] == 1:
            children.add(point[:i] + (0,) + point[i+1:])
        
    return children

# print(get_children((1, 1, 1, 1, 0)))

# def get_parents(point):
#     """
#     returns a set of the points covering the given point in the boolean poset
#     """
#     parents = set()
#     for i in range(n):
#         if point[i] == 0:
#             parents.add(point[:i] + (1,) + point[i+1:])
    
#     return parents

def flip(ideal, flip_peak):
    """
    returns a new ideal with the given peak flipped
    """
    reversed = tuple(1 - i for i in flip_peak) # reversed is guaranteed to be a peak (unless n = 1)
    
    if flip_peak in get_children(reversed):
            return None # ideal
        
    new_ideal = ideal.union({reversed}).difference(get_children(reversed) | {flip_peak}) # peaks can disappear if they're covered by reverse
    
    to_add = get_children(flip_peak)
    for child in get_children(flip_peak): # peaks can be added if they're children of flip_peak and not covered by anything else
        # print("child:", child)
        for peak in new_ideal:
            if sum(child[i] * peak[i] for i in range(n)) == sum(child): # peak covers child
                # print("peak that covers", peak)
                to_add = to_add.difference({child}) 
                
    return new_ideal.union(to_add)

# print("flipped", flip({(1, 1, 1, 1, 0)}, (1, 1, 1, 1, 0)))

def get_neighbors(starting_ideal):
    """
    returns a list of the neighbors
    """

    # return [flip(starting_ideal, peak) for peak in starting_ideal]
    return [flip(starting_ideal, peak) for peak in starting_ideal if flip(starting_ideal, peak) is not None]

# print(get_neighbors({(1, 0, 1, 1, 0), (1, 1, 1, 0, 0), (1, 1, 0, 1, 0), (0, 1, 1, 1, 0), (0, 0, 0, 0, 1)}))

def all_ideals():
    """
    returns a list of all ideals
    """
    start = {(1,) * (n-1) + (0,)}
    
    if n == 1:
        return [start]
    
    # use set of the peaks for visited list 
    visited = [start]
    to_do = [start]
    
    all_degrees = {} # dictionary of all degrees
    
    while to_do:
        ideal = to_do.pop()
        
        degree = len(get_neighbors(ideal))
        # print("get_neighbors is", get_neighbors(ideal))
        # print("all_degrees before", all_degrees)
        all_degrees[degree] = all_degrees.get(degree, 0) + 1
        # print("all_degrees after", all_degrees)
        
        for neighbor in get_neighbors(ideal):
            if neighbor not in visited: # does this have redundancy issues for the order of the peaks????
                to_do.append(neighbor)
                visited.append(neighbor)
    
    print(all_degrees)
    # print("length", len(visited))
    return visited

# for i in all_ideals():
#     print(i)

# for i in range(1, 7):
#     n = i
#     all_ideals()
# print(len(all_ideals()))

n = 5
print(1, get_neighbors({(1, 1, 1, 1, 0)}))
print(2, get_neighbors({(1, 0, 1, 1, 0), (1, 1, 1, 0, 0), (1, 1, 0, 1, 0), (0, 1, 1, 1, 0), (0, 0, 0, 0, 1)}))
print(3, get_neighbors({(0, 1, 0, 0, 1), (0, 1, 1, 1, 0), (1, 1, 1, 0, 0), (1, 1, 0, 1, 0)}))
print(4, get_neighbors({(1, 1, 1, 0, 0), (1, 1, 0, 1, 0), (0, 1, 0, 0, 1), (1, 0, 0, 0, 1), (0, 0, 1, 1, 0)}))
print(5, get_neighbors({(0, 0, 0, 1, 1), (1, 0, 1, 0, 0), (1, 1, 0, 1, 0), (0, 1, 0, 0, 1), (0, 1, 1, 0, 0), (1, 0, 0, 0, 1), (0, 0, 1, 1, 0)}))
print(6, get_neighbors({(1, 1, 0, 0, 1), (1, 1, 1, 0, 0), (1, 1, 0, 1, 0)}))
print(7, get_neighbors({(1, 0, 1, 0, 0), (0, 0, 1, 0, 1), (1, 1, 0, 0, 0), (0, 1, 0, 0, 1), (1, 0, 0, 0, 1), (0, 0, 1, 1, 0), (0, 0, 0, 1, 1), (0, 1, 0, 1, 0), (0, 1, 1, 0, 0), (1, 0, 0, 1, 0)}))