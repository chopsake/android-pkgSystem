<?php

class Graph
{
	var $graph_arr = array();
	var $solution_arr = array();

	function Graph()
	{
		//initialization of nodes, mythical for now
		$this->graph_arr = array();
	}

	function addNode($node)
	{
		global $debug;
		if ($node instanceof Node)
		{
			array_push($this->graph_arr, $node);
		}
		else
		{
			if ($debug)
				echo "<p>ERROR Graph.addNode(): parameter \$node is note an instance of Node.</p>";
		}
	}

	/** prints the entire graph, and all the edges **/
	function printGraph($graph_arr)
	{
		foreach ($graph_arr as $node)
		{
			echo $node->getNodeName();
			$adjacent_nodes = $node->getAdjacentNodes();
			foreach($adjacent_nodes as $a_node)
			{
				echo "<br /> -> " . $a_node;
			}
			echo "<br />";
		}
	}



	/**
	 * Recursive depth-first-search algorithm that looks for a path between $start_node_name and
	 * $finish_node_name. It sets the path in the global array $solution_arr;
	 *
	 * @param string $start_node_name - the starting node
	 * @param string $finish_node_name - the ending node
	 * @return null if no path found, Node if path was found
	 */
	function dfs_recursive($start_node_name, $finish_node_name)
	{
		$start_node = $this->getNode($start_node_name);		//obtain the Node object from the node name
		if ($start_node == null || !($start_node instanceof Node))
		{
			if ($debug)
				echo "<p>ERROR Graph.dfs_recursive(): Start node $start_node_name is not a valid node.</p>";
			return null;
		}

		$start_node->setVisited();							//set starting node to visited
		/* set the starting node to be part of the solution, and if it is not, it is removed later
		 */
		array_push($this->solution_arr, $start_node);

		$finish_node = $this->getNode($finish_node_name);	//obtain the Node object
		if ($finish_node == null || !($finish_node instanceof Node))
		{
			if ($debug)
				echo "<p>ERROR Graph.dfs_recursive(): Finish node $finish_node_name is not a valid node.</p>";
			return null;
		}

		if ($start_node_name == $finish_node_name)			//found the path! woohoo!
		{
			return $start_node;
		}

		$adjacent_nodes = $start_node->getAdjacentNodes();	//find $start_node's neighbors
		foreach($adjacent_nodes as $adjacent_node_name)
		{
			//neighbors are stored by name only to avoid circular references. Get the object
			$adjacent_node = $this->getNode($adjacent_node_name);
			if ($adjacent_node == null || !($adjacent_node instanceof Node))
			{
				if ($debug)
					echo "<p>WARN Graph.dfs_recursive(): Adjacent node $adjacent_node_name is not a valid node.</p>";
				continue;
			}
			//var_dump($adjacent_node);
			$is_node_visited = $adjacent_node->isVisited();
			if (!$is_node_visited)		//if this node was already visited, skip it. Otherwise, check the neighbor
			{
				//recursively check the neighbor
				$result_node = $this->dfs_recursive($adjacent_node_name, $finish_node_name);
				if ($result_node != null)
				{
					return $result_node;		//we found the path! return the result
				}
			}
		}
		//get rid of the latest "solution" node, since searching that path didn't yield a result
		array_pop($this->solution_arr);
		return null;		//no solution :( return null.

	}

	function getDfsSolution() { return $this->solution_arr; }

	//for debugging...
	function printDfsSolution()
	{
		if (is_array($this->solution_arr))
		{
			echo "Solution Array: <br /><br />";
			foreach ($this->solution_arr as $node)
			{
				echo "->" . $node->getNodeName();
			}
			echo "<br />";
		}
	}

        function getSolution()
        {
            $temp = array();
            if(is_array($this->solution_arr))
            {
                foreach($this->solution_arr as $node)
                    $temp[] = $node->getNodename();
            }
            return $temp;
        }

	function getNode($node_name)
	{
		foreach($this->graph_arr as $node)
		{
			if ($node->getNodeName() == $node_name)
			{
				return $node;
			}
		}
	}

	function getGraph() { return $this->graph_arr; }
}

class Node
{
	var $node_name;
	var $adjacent_nodes;
	var $is_visited;
	function Node($node_name, $adjacent_nodes)
	{
		$this->node_name = $node_name;
		$this->adjacent_nodes = $adjacent_nodes;
	}

	/** returns array of adjacent nodes **/
	function getAdjacentNodes()	{return $this->adjacent_nodes;}
	function getNodeName() {return $this->node_name;}
	function isVisited() { return $this->is_visited; }
	function setVisited(){ $this->is_visited = true; }
}

$sql = "SELECT w.user_id, w.friend_id
        FROM will_deliver w, (SELECT u.id
        FROM users u, (SELECT friend_id
        FROM friendships
        WHERE user_id = " . $userID . " ) f
        WHERE u.id = f.friend_id) fset
        WHERE w.user_id = fset.id
        UNION
        SELECT w1.user_id, w1.friend_id
        FROM will_deliver w1
        WHERE w1.user_id = " . $userID . ";";

$res = mysql_query($sql);
$num = mysql_num_rows($res);

if($num > 0)
{
    $output = array();
    $graph = new Graph();
    $count = 0;

    while($row = mysql_fetch_array($res))
    {
        $output[$count][0] = $row['user_id'];
        $output[$count][1] = $row['friend_id'];
        $count++;
    }

    // The known nodes array
    $nodesAr = array();

    // Setting up the nodes graph with their adjcent nodes
    foreach($output as $o)
    {
        $temp = array();
        if(!in_array($o[0], $nodesAr))
        {
            $nodesAr[] = $o[0];

            // Finding the adjacent nodes
            foreach($output as $adj)
            {
                if($adj[0] == $o[0])
                    if(!in_array($adj[1], $temp))
                    {
                        if($o[0] == $userID && $adj[1] == $dest);
                        else
                            $temp[] = $adj[1];
                    }
            }
            
            $n = new Node($o[0],$temp);
            $graph->addNode($n);
        }
        
    }

    // Finding nodes that are at the end of the chain to make the search work
    foreach($output as $o)
    {
        if(!in_array($o[1], $nodesAr))
        {
            $n = new Node($o[1], array());
            $graph->addNode($n);
            $nodesAr[] = $o[1];
        }
    }

    $graph->dfs_recursive($userID, $dest);
    $solution = $graph->getSolution();

}

?>
